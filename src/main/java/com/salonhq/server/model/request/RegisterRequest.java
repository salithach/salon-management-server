package com.salonhq.server.model.request;

import com.salonhq.server.dao.User;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class RegisterRequest {
    String name;
    String email;
    String username;
    String password;
    Set<String> roles;
    public User toUser() {
        String id = UUID.randomUUID().toString();
        return User.builder().id(id).name(name).username(username)
            .email(email).password(password).build();
    }
}