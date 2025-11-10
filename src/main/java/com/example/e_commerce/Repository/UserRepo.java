package com.example.e_commerce.Repository;

import com.example.e_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);
}
