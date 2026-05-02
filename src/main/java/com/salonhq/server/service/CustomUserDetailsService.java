package com.salonhq.server.service;

import com.salonhq.server.dao.User;
import com.salonhq.server.model.UserPrinciple;
import com.salonhq.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

import static com.salonhq.server.model.ErrorMessage.USER_NOT_FOUND;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("%s | user: %s", USER_NOT_FOUND.getValue(), username)));
        return UserPrinciple.build(user);
    }
}