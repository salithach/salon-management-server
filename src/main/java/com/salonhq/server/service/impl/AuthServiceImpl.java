package com.salonhq.server.service.impl;

import com.salonhq.server.dao.Role;
import com.salonhq.server.exception.CredentialException;
import com.salonhq.server.model.request.RegisterRequest;
import com.salonhq.server.model.RoleType;
import com.salonhq.server.dao.User;
import com.salonhq.server.model.response.TokenResponse;
import com.salonhq.server.service.AuthService;
import com.salonhq.server.service.UserService;
import com.salonhq.server.util.JwtUtil;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.salonhq.server.model.ErrorMessage.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(RegisterRequest registerRequest) {
        User userByUsername = userService.getByUsername(registerRequest.getUsername());
        User userByEmail = userService.getByEmail(registerRequest.getEmail());
        if (userByUsername != null) {
            throw new CredentialException(USER_NAME_TAKEN.getValue());
        } else if (userByEmail != null) {
            throw new CredentialException(USER_EMAIL_TAKEN.getValue());
        } else {
            return register(registerRequest);
        }
    }

    private User register(RegisterRequest registerRequest) {
        User user = registerRequest.toUser();
        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            roles.add(Role.builder().name(RoleType.ROLE_USER.name()).build());
        } else {
            Set<String> reqRoles = registerRequest.getRoles();
            reqRoles.forEach(role -> roles.add(
                Role.builder().name(role.toUpperCase()).build()
            ));
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.createUser(user);
    }

    @Override
    public TokenResponse loginUser(@NonNull String username, @NonNull String password) {
        User userExists = userService.getByUsername(username);
        if (userExists != null) {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtUtil.generateJwtToken(authentication, userExists);
        } else {
            throw new CredentialException(INVALID_USERNAME.getValue());
        }
    }
}