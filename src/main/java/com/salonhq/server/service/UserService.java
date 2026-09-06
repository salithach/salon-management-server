package com.salonhq.server.service;

import com.salonhq.server.dao.User;

import java.util.List;

public interface UserService {
    User getByUserById(String userId);
    User getByUsername(String username);
    User getByEmail(String email);
    User createUser(User user);
    List<User> getUsers();
    User activateUser(String userId);
}
