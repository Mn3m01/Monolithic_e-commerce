package com.example.e_commerce.service.auth;

import com.example.e_commerce.dto.auth.LoginDTO;
import com.example.e_commerce.dto.auth.RegisterDTO;
import com.example.e_commerce.entity.Role;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user and return a JWT token.
     */
    public String register(RegisterDTO registerDTO) {
        // Validate uniqueness
        if (userRepo.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        // Create user
        User user = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.USER)
                .build();

        userRepo.save(user);

        // Generate JWT
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        return jwtService.generateToken(userDetails);
    }

    /**
     * Authenticate a user and return a JWT token.
     */
    public String authenticate(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        User user = userRepo.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        return jwtService.generateToken(userDetails);
    }
}
