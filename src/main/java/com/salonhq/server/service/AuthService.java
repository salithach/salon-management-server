package com.salonhq.server.service;

import com.salonhq.server.dao.User;
import com.salonhq.server.model.request.UserRegistrationRequest;
import com.salonhq.server.model.request.SalonRegistrationRequest;
import com.salonhq.server.model.response.TokenResponse;

public interface AuthService {
    User registerSalon(SalonRegistrationRequest salonRegistrationRequest);
    User registerUser(UserRegistrationRequest userRegistrationRequest);
    TokenResponse loginUser(String username, String password);
}
