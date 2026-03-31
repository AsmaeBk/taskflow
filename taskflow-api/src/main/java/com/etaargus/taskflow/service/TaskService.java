package com.etaargus.taskflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etaargus.taskflow.dto.TaskResponseDTO;
import com.etaargus.taskflow.model.Task;
import com.etaargus.taskflow.repository.TaskRepository;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponseDTO saveTask(Task task) {
        Task savedTask = taskRepository.save(task);
        return mapToDTO(savedTask);
    }

    public List<TaskResponseDTO> getTaskList(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream().map(this::mapToDTO).toList();
    }

    public TaskResponseDTO updateTask(Long id, Task taskDetails, Long userId) {
    
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (!existingTask.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setCompleted(taskDetails.isCompleted());

        Task updatedTask = taskRepository.save(existingTask);
        return mapToDTO(updatedTask);
    }

    public void deleteTask(Long id, Long userId) {

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        
        if (!task.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        taskRepository.deleteById(id);
    }

    private TaskResponseDTO mapToDTO(Task task) {
        return new TaskResponseDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.isCompleted()
        );
    }
}
