package com.taskmanager.dto.request;

import com.taskmanager.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreateRequest {
    
    @NotBlank
    private String title;
    
    private String description;
    
    @NotNull
    private Priority priority;
    
    private LocalDateTime dueDate;
    
    private Long assignedToUserId;
}
