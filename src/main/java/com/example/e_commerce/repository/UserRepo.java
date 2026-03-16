package com.example.e_commerce.repository;

import com.example.e_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);
}
