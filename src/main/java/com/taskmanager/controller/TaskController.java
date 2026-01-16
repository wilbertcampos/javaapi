package com.taskmanager.controller;

import com.taskmanager.dto.request.TaskCreateRequest;
import com.taskmanager.dto.request.TaskUpdateRequest;
import com.taskmanager.dto.response.TaskResponse;
import com.taskmanager.dto.response.ApiResponse;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.entity.Priority;
import com.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tasks", description = "Task management APIs")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task with the provided details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Task created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskCreateRequest request) {
        log.info("Creating new task: {}", request);
        TaskResponse taskResponse = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task created successfully", taskResponse));
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieves a paginated list of tasks with optional filters")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<TaskResponse>>> getTasks(
            @Parameter(description = "Filter by task status") @RequestParam(required = false) TaskStatus status,
            @Parameter(description = "Filter by priority") @RequestParam(required = false) Priority priority,
            @Parameter(description = "Filter by assigned user ID") @RequestParam(required = false) Long assignedToUserId,
            @Parameter(description = "Search by title or description") @RequestParam(required = false) String search,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("Getting tasks with filters - status: {}, priority: {}, assignedToUserId: {}, search: {}", 
                status, priority, assignedToUserId, search);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<TaskResponse> tasks = taskService.getTasks(status, priority, assignedToUserId, search, pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", tasks));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieves a specific task by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @Parameter(description = "Task ID") @PathVariable Long id) {
        log.info("Getting task by id: {}", id);
        TaskResponse taskResponse = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.success("Task retrieved successfully", taskResponse));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Updates an existing task with the provided details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request) {
        log.info("Updating task with id: {}", id);
        TaskResponse taskResponse = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.success("Task updated successfully", taskResponse));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Deletes a task by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<ApiResponse<String>> deleteTask(
            @Parameter(description = "Task ID") @PathVariable Long id) {
        log.info("Deleting task with id: {}", id);
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success("Task deleted successfully", "Task deleted successfully"));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update task status", description = "Updates the status of a specific task")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @Parameter(description = "New task status") @RequestParam TaskStatus status) {
        log.info("Updating task status for id: {} to status: {}", id, status);
        TaskResponse taskResponse = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Task status updated successfully", taskResponse));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user tasks", description = "Retrieves all tasks assigned to a specific user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User tasks retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<TaskResponse>>> getUserTasks(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("Getting tasks for user: {}", userId);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<TaskResponse> tasks = taskService.getUserTasks(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success("User tasks retrieved successfully", tasks));
    }
}
