package com.etaargus.taskflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etaargus.taskflow.model.Task;
import com.etaargus.taskflow.repository.TaskRepository;
import com.etaargus.taskflow.security.JwtUtil;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;

    private final JwtUtil jwtUtil;

    public TaskService(TaskRepository taskRepository, JwtUtil jwtUtil) {
        this.taskRepository = taskRepository;
        this.jwtUtil = jwtUtil;
    }

    /*
     * Saves a new task to the database, associating it with the authenticated user.
     * @param task The task to be saved.
     * @param header The Authorization header containing the JWT token.
     * @return The saved task with its generated ID and associated user ID.
     */
    public Task saveTask(Task task, String header) {
    // Check if header is valid and starts with Bearer
    if (header != null && header.startsWith("Bearer ")) {
        // Remove "Bearer " (7 characters) to get only the token
        String token = header.substring(7);
        
        Long idFromToken = jwtUtil.extractUserId(token); // Now it's clean!
        task.setUserId(idFromToken);
        return taskRepository.save(task);
    }
    throw new RuntimeException("Invalid or missing Authorization header");
}
    /*
     * Retrieves a list of tasks for the authenticated user.
     * @param header The Authorization header containing the JWT token.
     * @return A list of tasks associated with the authenticated user.
     */
    public List<Task> getTaskList(String header) {

        // Remove "Bearer " prefix if present

        String token = header.replace("Bearer ", "");
        // Reject if invalid
        if (!jwtUtil.isValid(token)) {
           throw new RuntimeException("Unauthorized");
        }
        // Extract userId from token
        Long userId = jwtUtil.extractUserId(token);
        
        return taskRepository.findByUserId(userId);
    }

    /*
     * Modifies a task based on its ID and the provided entity.
     * @param id The ID of the task to modify.
     * @param entity The updated task information.
     * @return A message indicating the result of the operation.
     */
    public Task updateTask(Long id, Task taskDetails, String header) {
        
    
        // Find the existing task
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        // Update the fields
        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setCompleted(taskDetails.isCompleted());

        // Save and return
        return taskRepository.save(existingTask);
    }

    /*
     * Deletes a task based on its ID.
     * @param id The ID of the task to delete.
     */
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
