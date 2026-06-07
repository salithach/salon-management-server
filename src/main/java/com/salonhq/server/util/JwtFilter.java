package com.salonhq.server.util;

import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.model.response.ErrorResponse;
import com.salonhq.server.model.tenant.TenantContext;
import com.salonhq.server.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static com.salonhq.server.util.Constants.AUTH_HEADER;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(CustomUserDetailsService userDetailsService , JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException
    {
        try {
            if (!authenticateRequest(request, response)) {
                return;
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Validates the JWT from the request and sets up the security context.
     * Returns true if the request should proceed, false if an unauthorized response was written.
     */
    private boolean authenticateRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String header = request.getHeader(AUTH_HEADER);
        if (header == null || !header.startsWith(String.format("%s ", Constants.BEARER))) {
            return true;
        }
        String token = header.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                TenantContext.setTenant(username);
            }
            return true;
        } catch (ExpiredJwtException ex) {
            writeUnauthorizedResponse(response, "Token expired, please login again");
            return false;
        } catch (JwtException ex) {
            writeUnauthorizedResponse(response, "Invalid token");
            return false;
        }
    }

    private void writeUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        EnvelopedResponse<Object> body = new EnvelopedResponse<>();
        body.setErrors(List.of(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message)));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}