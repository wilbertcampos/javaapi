package com.taskmanager.repository;

import com.taskmanager.entity.Priority;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    Page<Task> findByAssignedToIdAndDeletedFalse(Long userId, Pageable pageable);
    
    Page<Task> findByDeletedFalse(Pageable pageable);
    
    Page<Task> findByStatusAndDeletedFalse(TaskStatus status, Pageable pageable);
    
    Page<Task> findByPriorityAndDeletedFalse(Priority priority, Pageable pageable);
    
    Page<Task> findByAssignedToIdAndStatusAndDeletedFalse(Long userId, TaskStatus status, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.deleted = false AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Task> searchTasks(@Param("keyword") String keyword, Pageable pageable);
}
