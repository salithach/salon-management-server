package com.salonhq.server.service;

import com.salonhq.server.dao.User;
import com.salonhq.server.model.request.RegisterRequest;
import com.salonhq.server.model.response.TokenResponse;

public interface AuthService {
    User registerUser(RegisterRequest registerRequest);
    TokenResponse loginUser(String username, String password);
}
