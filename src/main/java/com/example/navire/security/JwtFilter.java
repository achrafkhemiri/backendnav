package com.example.navire.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = null;
        String mail = null;

        // Lire le token JWT depuis le cookie HTTPOnly 'jwt'
        if (request.getCookies() != null) {
            logger.debug("Incoming cookies for {}: {}", request.getRequestURI(), java.util.Arrays.toString(request.getCookies()));
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        } else {
            logger.debug("No cookies present for request {}", request.getRequestURI());
        }

        // If no cookie token found, try Authorization header Bearer token as a fallback
        if (token == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                // Do not log the token value; just note presence
                logger.debug("Authorization header Bearer token present for {}", request.getRequestURI());
                token = authHeader.substring(7);
            } else {
                logger.debug("No Authorization Bearer header for {}", request.getRequestURI());
            }
        }

        if (token != null) {
            try {
                mail = jwtUtil.extractMail(token);
            } catch (Exception e) {
                logger.warn("Failed to extract mail from JWT: {}", e.getMessage());
            }
        } else {
            logger.debug("No JWT token found in cookies for {}", request.getRequestURI());
        }

        if (mail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean valid = false;
            try {
                valid = jwtUtil.validateToken(token, mail);
            } catch (Exception e) {
                logger.warn("Error validating token for mail {}: {}", mail, e.getMessage());
            }
            if (valid) {
                logger.debug("JWT validated for user {} on {}", mail, request.getRequestURI());
                UserDetails userDetails = new User(mail, "", Collections.emptyList());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.debug("JWT invalid or not validated for request {}", request.getRequestURI());
            }
        }
        filterChain.doFilter(request, response);
    }
}
