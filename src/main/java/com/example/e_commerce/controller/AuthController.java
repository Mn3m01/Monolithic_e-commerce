package com.example.e_commerce.controller;

import com.example.e_commerce.dto.auth.LoginDTO;
import com.example.e_commerce.dto.auth.RegisterDTO;
import com.example.e_commerce.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid@RequestBody RegisterDTO registerDTO) {
        String token = authService.register(registerDTO);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid@RequestBody LoginDTO loginDTO) {
        String token = authService.authenticate(loginDTO);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
