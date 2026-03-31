package com.etaargus.taskflow.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etaargus.taskflow.dto.TaskRequestDTO;
import com.etaargus.taskflow.dto.TaskResponseDTO;
import com.etaargus.taskflow.model.Task;
import com.etaargus.taskflow.service.TaskService;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponseDTO> getTasks(@RequestAttribute("userId") Long userId) {
        return taskService.getTaskList(userId);
    }

    @PostMapping
    public TaskResponseDTO createTask(@jakarta.validation.Valid @RequestBody TaskRequestDTO dto, @RequestAttribute("userId") Long userId) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());
        task.setUserId(userId);
        return taskService.saveTask(task);
    }
    
    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(
        @PathVariable Long id,
        @RequestBody Task taskDetails,
        @RequestAttribute("userId") Long userId
    ) {
        return taskService.updateTask(id, taskDetails, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        taskService.deleteTask(id, userId);
    }
}