package com.salonhq.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salonhq.server.dao.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrinciple implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;
    String id;
    String name;
    String username;
    String email;
    @JsonIgnore
    String password;
    Collection<? extends GrantedAuthority> authorities;

    public static UserPrinciple build(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .toList();
        return new UserPrinciple(
            user.getId(),
            user.getName(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
