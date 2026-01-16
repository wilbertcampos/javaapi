package com.taskmanager.service;

import com.taskmanager.dto.TaskCreateRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.dto.TaskUpdateRequest;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.enums.Priority;
import com.taskmanager.enums.TaskStatus;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.UnauthorizedException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        log.info("Creating task with title: {}", request.getTitle());
        
        String currentUserEmail = getCurrentUserEmail();
        Task task = taskMapper.toEntity(request);
        
        task.setCreatedBy(currentUserEmail);
        task.setStatus(TaskStatus.TODO);
        task.setDeleted(false);
        
        if (request.getAssignedToUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToUserId()));
            task.setAssignedTo(assignedUser);
            
            // Send async email notification
            emailService.sendTaskAssignedEmail(assignedUser.getEmail(), task.getTitle());
        }
        
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());
        
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        log.info("Fetching task with id: {}", id);
        
        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        if (!isAuthorized(task)) {
            throw new UnauthorizedException("You are not authorized to access this task");
        }
        
        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        log.info("Updating task with id: {}", id);
        
        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        if (!isAuthorized(task)) {
            throw new UnauthorizedException("You are not authorized to update this task");
        }
        
        TaskStatus oldStatus = task.getStatus();
        taskMapper.updateEntityFromRequest(request, task);
        task.setUpdatedBy(getCurrentUserEmail());
        
        if (request.getStatus() != null && request.getStatus() == TaskStatus.DONE && oldStatus != TaskStatus.DONE) {
            task.setCompletedAt(LocalDateTime.now());
            if (task.getAssignedTo() != null) {
                emailService.sendTaskCompletedEmail(task.getAssignedTo().getEmail(), task.getTitle());
            }
        }
        
        Task savedTask = taskRepository.save(task);
        log.info("Task updated successfully with id: {}", savedTask.getId());
        
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        
        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        String currentUserEmail = getCurrentUserEmail();
        if (!task.getCreatedBy().equals(currentUserEmail) && !isAdmin()) {
            throw new UnauthorizedException("Only task creator or admin can delete this task");
        }
        
        task.setDeleted(true);
        taskRepository.save(task);
        log.info("Task deleted successfully with id: {}", id);
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(Long id, TaskStatus status) {
        log.info("Updating task status with id: {} to status: {}", id, status);
        
        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        if (!isAuthorized(task)) {
            throw new UnauthorizedException("You are not authorized to update this task status");
        }
        
        TaskStatus oldStatus = task.getStatus();
        task.setStatus(status);
        task.setUpdatedBy(getCurrentUserEmail());
        
        if (status == TaskStatus.DONE && oldStatus != TaskStatus.DONE) {
            task.setCompletedAt(LocalDateTime.now());
            if (task.getAssignedTo() != null) {
                emailService.sendTaskCompletedEmail(task.getAssignedTo().getEmail(), task.getTitle());
            }
        }
        
        Task savedTask = taskRepository.save(task);
        log.info("Task status updated successfully with id: {}", savedTask.getId());
        
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasks(TaskStatus status, Priority priority, Long assignedToUserId, String search, Pageable pageable) {
        log.info("Fetching tasks with filters - status: {}, priority: {}, assignedToUserId: {}, search: {}", 
                status, priority, assignedToUserId, search);
        
        Page<Task> tasks;
        
        if (search != null && !search.trim().isEmpty()) {
            tasks = taskRepository.searchTasks(search, pageable);
        } else if (status != null && priority != null && assignedToUserId != null) {
            tasks = taskRepository.findByStatusAndPriorityAndAssignedTo_IdAndDeletedFalse(status, priority, assignedToUserId, pageable);
        } else if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriorityAndDeletedFalse(status, priority, pageable);
        } else if (status != null && assignedToUserId != null) {
            tasks = taskRepository.findByStatusAndAssignedTo_IdAndDeletedFalse(status, assignedToUserId, pageable);
        } else if (priority != null && assignedToUserId != null) {
            tasks = taskRepository.findByPriorityAndAssignedTo_IdAndDeletedFalse(priority, assignedToUserId, pageable);
        } else if (status != null) {
            tasks = taskRepository.findByStatusAndDeletedFalse(status, pageable);
        } else if (priority != null) {
            tasks = taskRepository.findByPriorityAndDeletedFalse(priority, pageable);
        } else if (assignedToUserId != null) {
            tasks = taskRepository.findByAssignedTo_IdAndDeletedFalse(assignedToUserId, pageable);
        } else {
            tasks = taskRepository.findByDeletedFalse(pageable);
        }
        
        return tasks.map(taskMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getUserTasks(Long userId, Pageable pageable) {
        log.info("Fetching tasks for user with id: {}", userId);
        
        Page<Task> tasks = taskRepository.findByAssignedTo_IdAndDeletedFalse(userId, pageable);
        return tasks.map(taskMapper::toResponse);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private boolean isAuthorized(Task task) {
        String currentUserEmail = getCurrentUserEmail();
        boolean isAssignedTo = task.getAssignedTo() != null && task.getAssignedTo().getEmail().equals(currentUserEmail);
        return isAssignedTo || isAdmin();
    }
}
