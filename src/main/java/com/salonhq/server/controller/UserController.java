package com.salonhq.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

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
}
