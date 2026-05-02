package com.salonhq.server.repository;

import com.salonhq.server.dao.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserEmail(String email);
    User save(User user);
}
