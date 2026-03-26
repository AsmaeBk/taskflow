package com.etaargus.taskflow.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.etaargus.taskflow.model.User;


public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
    
}
