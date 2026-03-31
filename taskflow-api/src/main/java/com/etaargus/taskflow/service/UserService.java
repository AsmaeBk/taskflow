package com.etaargus.taskflow.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.etaargus.taskflow.dto.LoginResponseDTO;
import com.etaargus.taskflow.dto.UserResponseDTO;
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
    
    public UserResponseDTO register(User user) {
        // Encode the password before saving!
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getEmail());
    }

    public LoginResponseDTO login(User user) {
        User userDB = userRepository.findByEmail(user.getEmail());
        
        if (userDB == null) {
            throw new RuntimeException("User not found");
        }

        // check if password matches
        if (!passwordEncoder.matches(user.getPassword(), userDB.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // if login successful → generate JWT token
        String token = jwtUtil.generateToken(userDB.getId());
        return new LoginResponseDTO(token);
    }
    
}
