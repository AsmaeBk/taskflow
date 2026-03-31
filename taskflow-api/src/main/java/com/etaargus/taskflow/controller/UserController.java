package com.etaargus.taskflow.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.etaargus.taskflow.dto.LoginResponseDTO;
import com.etaargus.taskflow.dto.UserResponseDTO;
import com.etaargus.taskflow.model.User;
import com.etaargus.taskflow.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody User entity) {  
        return userService.register(entity);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody User entity) {
        return userService.login(entity);
    }
    
    
}
