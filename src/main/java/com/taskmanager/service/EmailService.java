package com.taskmanager.service;

public interface EmailService {
    void sendTaskAssignmentEmail(String toEmail, String taskTitle, String assignerName);
    
    void sendTaskCompletionEmail(String toEmail, String taskTitle);
}
