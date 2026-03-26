package com.etaargus.taskflow.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.etaargus.taskflow.model.User;
import com.etaargus.taskflow.service.UserService;


@RestController
@RequestMapping("/auth")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User entity) {  
        userService.register(entity);      
        return entity;
    }

    @PostMapping("/login")
    public String postMethodName(@RequestBody User entity) {
        return userService.login(entity);
    }
    
    
}
