package com.taskmanager.service;

import com.taskmanager.dto.TaskCreateRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.dto.TaskUpdateRequest;
import com.taskmanager.model.Priority;
import com.taskmanager.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponse createTask(TaskCreateRequest request);
    
    TaskResponse getTaskById(Long id);
    
    TaskResponse updateTask(Long id, TaskUpdateRequest request);
    
    void deleteTask(Long id);
    
    TaskResponse updateTaskStatus(Long id, TaskStatus status);
    
    Page<TaskResponse> getTasks(TaskStatus status, Priority priority, Long assignedToUserId, String search, Pageable pageable);
    
    Page<TaskResponse> getUserTasks(Long userId, Pageable pageable);
}
