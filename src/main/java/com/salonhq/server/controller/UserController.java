package com.salonhq.server.controller;

import com.salonhq.server.dao.User;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUserInfo(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(user)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.getUsers();
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(users)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserByUserById(@PathVariable String userId) {
        User user = userService.getByUserById(userId);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(user)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{userId}/activate")
    public ResponseEntity<?> activateUser(@PathVariable String userId) {
        User user = userService.activateUser(userId);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(user)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
