package com.smartpark.web.controller;

import com.smartpark.domain.dto.LoginResponse;
import com.smartpark.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "1234";
    public static final String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password";
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {


        if (username.equals(USER_NAME)
                && password.equals(PASSWORD)) {

            return jwtService.generateToken(username);
        }

        throw new RuntimeException(INVALID_USERNAME_OR_PASSWORD);
    }
}
