package com.etaargus.taskflow.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etaargus.taskflow.model.Task;
import com.etaargus.taskflow.service.TaskService;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<Task> getTasks(@RequestHeader("Authorization") String header) {
        return taskService.getTaskList(header);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, @RequestHeader("Authorization") String header) {
        return taskService.saveTask(task, header);
    }
    
    @PutMapping("/{id}")
    public Task updateTask(
        @PathVariable Long id, 
        @RequestBody Task taskDetails,
        @RequestHeader("Authorization") String header
    ) {
        // Pass the ID, the new data, and the header to your service
        return taskService.updateTask(id, taskDetails, header);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, @RequestHeader("Authorization") String header) {
        // You can add logic here to verify the user owns the task before deleting
        taskService.deleteTask(id);
    }
}