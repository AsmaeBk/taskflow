package com.etaargus.taskflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etaargus.taskflow.model.Task;
import com.etaargus.taskflow.repository.TaskRepository;
import com.etaargus.taskflow.security.JwtUtil;

@Service
public class TaskService {
    
    private final TaskRepository tasRepository;

    private final JwtUtil jwtUtil;

    public TaskService(TaskRepository tasRepository, JwtUtil jwtUtil) {
        this.tasRepository = tasRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<Task> getTaskList(String header) {

        // Remove "Bearer " prefix if present

        String token = header.replace("Bearer ", "");
        // Reject if invalid
        if (!jwtUtil.isValid(token)) {
           throw new RuntimeException("Unauthorized");
        }
        // Extract userId from token
        Long userId = jwtUtil.extractUserId(token);
        
        return tasRepository.findByUserId(userId);
    }
}
