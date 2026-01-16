package com.taskmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    @Async
    public void sendTaskAssignmentEmail(String toEmail, String taskTitle, String assignerName) {
        log.info("Sending task assignment email to {} for task: {}", toEmail, taskTitle);
        
        if (mailSender == null) {
            log.warn("JavaMailSender not available. Email not sent (demo mode).");
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@taskmanager.com");
            message.setTo(toEmail);
            message.setSubject("New Task Assignment: " + taskTitle);
            message.setText(String.format(
                "Hello,\n\n" +
                "You have been assigned a new task by %s.\n\n" +
                "Task: %s\n\n" +
                "Please log in to the Task Manager to view the details.\n\n" +
                "Best regards,\n" +
                "Task Manager Team",
                assignerName, taskTitle
            ));
            
            mailSender.send(message);
            log.info("Task assignment email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send task assignment email to {}: {}", toEmail, e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendTaskCompletionEmail(String toEmail, String taskTitle) {
        log.info("Sending task completion email to {} for task: {}", toEmail, taskTitle);
        
        if (mailSender == null) {
            log.warn("JavaMailSender not available. Email not sent (demo mode).");
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@taskmanager.com");
            message.setTo(toEmail);
            message.setSubject("Task Completed: " + taskTitle);
            message.setText(String.format(
                "Hello,\n\n" +
                "The following task has been marked as completed:\n\n" +
                "Task: %s\n\n" +
                "Please log in to the Task Manager to view the details.\n\n" +
                "Best regards,\n" +
                "Task Manager Team",
                taskTitle
            ));
            
            mailSender.send(message);
            log.info("Task completion email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send task completion email to {}: {}", toEmail, e.getMessage(), e);
        }
    }
}
