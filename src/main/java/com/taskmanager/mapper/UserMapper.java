package com.taskmanager.mapper;

import com.taskmanager.dto.response.UserResponse;
import com.taskmanager.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserResponse toResponse(User user);
    
    List<UserResponse> toResponseList(List<User> users);
}
