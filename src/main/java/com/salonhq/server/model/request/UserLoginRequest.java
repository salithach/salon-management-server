package com.salonhq.server.model.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    String username;
    String password;
}