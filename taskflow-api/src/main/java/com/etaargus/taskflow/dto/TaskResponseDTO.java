package com.etaargus.taskflow.dto;

public class TaskResponseDTO {

     private Long id;
    private String title;
    private String description;
    private boolean completed;

    public TaskResponseDTO(Long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // getters
    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public boolean isCompleted() {
        return completed;
    }
    
    
}
