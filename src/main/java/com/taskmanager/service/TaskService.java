package com.taskmanager.service;

import com.taskmanager.dto.request.TaskCreateRequest;
import com.taskmanager.dto.request.TaskUpdateRequest;
import com.taskmanager.dto.response.TaskResponse;
import com.taskmanager.entity.Priority;
import com.taskmanager.entity.TaskStatus;
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
