package com.salonhq.server.service;

import com.salonhq.server.dao.User;
import com.salonhq.server.model.request.UserRegisterRequest;
import com.salonhq.server.model.request.salon.SalonRegisterRequest;
import com.salonhq.server.model.response.TokenResponse;

public interface AuthService {
    User registerSalon(SalonRegisterRequest salonRegisterRequest);
    User registerUser(UserRegisterRequest userRegisterRequest);
    TokenResponse loginUser(String username, String password);
}
