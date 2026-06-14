package com.salonhq.server.model.request;

import com.salonhq.server.dao.User;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserRegisterRequest {
    protected String email;
    protected String username;
    protected String password;
    Set<String> roles;
    public User toUser() {
        String id = UUID.randomUUID().toString();
        return User.builder()
            .id(id)
            .username(username)
            .email(email)
            .password(password)
        .build();
    }
}
