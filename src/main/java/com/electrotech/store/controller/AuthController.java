package com.electrotech.store.controller;

import com.electrotech.store.model.User;
import com.electrotech.store.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody Map<String, String> data) {
        return authService.registerUser(
                data.get("email"),
                data.get("password"),
                data.get("firstName"),
                data.get("lastName")
        );
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> data) {
        User user = authService.login(data.get("email"), data.get("password"));
        if (user == null) {
            throw new RuntimeException("Email sau parolă incorectă!");
        }
        return user;
    }
}