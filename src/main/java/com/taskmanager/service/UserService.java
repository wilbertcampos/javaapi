package com.taskmanager.service;

import com.taskmanager.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse getCurrentUser();
    
    UserResponse updateCurrentUser(String fullName, String email);
    
    UserResponse getUserById(Long id);
    
    Page<UserResponse> getAllUsers(Pageable pageable);
}
