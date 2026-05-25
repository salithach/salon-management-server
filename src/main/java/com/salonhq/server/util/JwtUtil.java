package com.salonhq.server.util;

import com.salonhq.server.dao.Role;
import com.salonhq.server.dao.User;
import com.salonhq.server.model.UserPrinciple;
import com.salonhq.server.model.response.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.salonhq.server.util.Constants.ROLES;

@Component
public class JwtUtil {

    @Value("${salonhq.jwt.secret}")
    private String jwtSecret;

    @Value("${salonhq.jwt.expiration}")
    private String jwtExpirationTime;

    public TokenResponse generateJwtToken(Authentication authentication, User user) {
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        if (userPrincipal != null) {
            String username = userPrincipal.getUsername();
            List<String> roles = user.getRoles().stream().map(Role::getName).toList();
            String token = Jwts.builder()
                .setSubject(username)
                .claim(ROLES, roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis() + Long.parseLong(jwtExpirationTime))))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
            .compact();
            return TokenResponse.builder().username(username).roles(roles).token(token).build();
        }
        return null;
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret.getBytes())
            .build()
            .parseClaimsJws(token)
        .getBody();
    }
}
