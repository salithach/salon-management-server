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

    @GetMapping("/hello")
    public String user() {
        return "Hello User";
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/hello-user")
    public String userTest() {
        return "Hello User Restricted";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/hello-admin")
    public String adminTest() {
        return "Hello Admin Restricted";
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(user)
            .errors(List.of())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
