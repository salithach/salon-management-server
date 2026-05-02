package com.salonhq.server.service;

import com.salonhq.server.dao.User;

public interface UserService {
    User getByUsername(String username);
    User getByEmail(String email);
    User createUser(User user);
}
