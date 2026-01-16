package com.taskmanager.service;

import com.taskmanager.dto.request.TaskCreateRequest;
import com.taskmanager.dto.response.TaskResponse;
import com.taskmanager.entity.Priority;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.entity.User;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private User user;
    private TaskCreateRequest taskCreateRequest;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("user@example.com")
                .fullName("Test User")
                .build();

        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.TODO)
                .priority(Priority.MEDIUM)
                .assignedTo(user)
                .createdBy("admin@example.com")
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();

        taskCreateRequest = new TaskCreateRequest();
        taskCreateRequest.setTitle("Test Task");
        taskCreateRequest.setDescription("Test Description");
        taskCreateRequest.setPriority(Priority.MEDIUM);
        taskCreateRequest.setAssignedToUserId(1L);

        taskResponse = new TaskResponse();
        taskResponse.setId(1L);
        taskResponse.setTitle("Test Task");
        taskResponse.setDescription("Test Description");
        taskResponse.setStatus(TaskStatus.TODO);
        taskResponse.setPriority(Priority.MEDIUM);

        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("admin@example.com");
    }

    @Test
    void testCreateTask_Success() {
        when(taskMapper.toEntity(taskCreateRequest)).thenReturn(task);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.createTask(taskCreateRequest);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertEquals(Priority.MEDIUM, result.getPriority());

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(userRepository, times(1)).findById(1L);
        verify(emailService, times(1)).sendTaskAssignmentEmail(anyString(), anyString(), anyString());
        verify(taskMapper, times(1)).toEntity(taskCreateRequest);
        verify(taskMapper, times(1)).toResponse(task);
    }

    @Test
    void testGetTaskById_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);
        when(authentication.getAuthorities()).thenAnswer(invocation -> 
                java.util.Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        TaskResponse result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Task", result.getTitle());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskMapper, times(1)).toResponse(task);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(999L);
        });

        assertNotNull(exception);
        verify(taskRepository, times(1)).findById(999L);
        verify(taskMapper, never()).toResponse(any(Task.class));
    }
}
