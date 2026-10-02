package com.salonhq.server.controller;

import com.salonhq.server.model.request.UserLoginRequest;
import com.salonhq.server.dao.User;
import com.salonhq.server.model.request.UserRegistrationRequest;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.model.request.SalonRegistrationRequest;
import com.salonhq.server.model.response.TokenResponse;
import com.salonhq.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        TokenResponse loginResult = authService.loginUser(request.getUsername(), request.getPassword());
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(loginResult)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/registerSalon")
    public ResponseEntity<?> registerSalon(@RequestBody SalonRegistrationRequest salonRegistrationRequest) {
        User userResult = authService.registerSalon(salonRegistrationRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(userResult)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        User userResult = authService.registerUser(userRegistrationRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(userResult)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
