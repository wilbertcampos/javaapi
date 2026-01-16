package com.taskmanager.controller;

import com.taskmanager.dto.request.LoginRequest;
import com.taskmanager.dto.request.RegisterRequest;
import com.taskmanager.dto.response.ApiResponse;
import com.taskmanager.dto.response.AuthResponse;
import com.taskmanager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns authentication tokens")
    @ApiResponses(value = {
        @SwaggerApiResponse(responseCode = "201", description = "User registered successfully"),
        @SwaggerApiResponse(responseCode = "400", description = "Invalid input data"),
        @SwaggerApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        AuthResponse authResponse = authService.register(request);
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(authResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates user credentials and returns authentication tokens")
    @ApiResponses(value = {
        @SwaggerApiResponse(responseCode = "200", description = "Login successful"),
        @SwaggerApiResponse(responseCode = "401", description = "Invalid credentials"),
        @SwaggerApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("User login attempt for email: {}", request.getEmail());
        AuthResponse authResponse = authService.login(request);
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(authResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Generates a new access token using a valid refresh token")
    @ApiResponses(value = {
        @SwaggerApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @SwaggerApiResponse(responseCode = "401", description = "Invalid or expired refresh token"),
        @SwaggerApiResponse(responseCode = "400", description = "Missing or invalid authorization header")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestHeader("Authorization") String authHeader) {
        log.info("Token refresh requested");
        String refreshToken = extractTokenFromHeader(authHeader);
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(authResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidates the user's current authentication token")
    @ApiResponses(value = {
        @SwaggerApiResponse(responseCode = "200", description = "Logout successful"),
        @SwaggerApiResponse(responseCode = "401", description = "Invalid or expired token"),
        @SwaggerApiResponse(responseCode = "400", description = "Missing or invalid authorization header")
    })
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
        log.info("User logout requested");
        String token = extractTokenFromHeader(authHeader);
        authService.logout(token);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Logout successful")
                .data("User logged out successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid authorization header format");
    }
}
