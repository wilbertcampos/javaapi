package com.taskmanager.dto.request;

import com.taskmanager.entity.Priority;
import com.taskmanager.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateRequest {
    
    private String title;
    
    private String description;
    
    private TaskStatus status;
    
    private Priority priority;
    
    private LocalDateTime dueDate;
}
