package com.etaargus.taskflow.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.etaargus.taskflow.model.User;
import com.etaargus.taskflow.repository.UserRepository;
import com.etaargus.taskflow.security.JwtUtil;

@Service
public class UserService {

    private final JwtUtil jwtUtil;

    // inject repository to access db
    private final UserRepository userRepository;

    // password encoder (to hash passwords before saving to database)
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    
    public ResponseEntity<?> register(User user) {

        // Encode the password before saving!
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    public String login(User user) {
        User userDB = userRepository.findByEmail(user.getEmail());
        
        if (userDB == null) {
            throw new RuntimeException("User not found");
        }

        // check if password matches
        if (!passwordEncoder.matches(user.getPassword(), userDB.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // if login successful → generate JWT token
        return jwtUtil.generateToken(userDB.getId());
    }
    
}
