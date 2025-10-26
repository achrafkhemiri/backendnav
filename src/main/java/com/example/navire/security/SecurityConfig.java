package com.example.navire.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;

        @Bean
        public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
            org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
                // Allow origin patterns (helps with Electron file:// or app origins during development)
                // In production, restrict this to your real front-end origin(s).
                configuration.setAllowedOriginPatterns(java.util.List.of("*"));
            configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(java.util.List.of("*"));
            configuration.setAllowCredentials(true);
            org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/api/users/login",
                            "/api/users/register",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/v2/api-docs",
                            "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
            // .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
    }
    // @Bean
    // public AccessDeniedHandler accessDeniedHandler() {
    //     return (request, response, accessDeniedException) -> {
    //         // Log details about the forbidden access
    //         System.err.println("403 Forbidden: " + request.getRequestURI() + " | Reason: " + accessDeniedException.getMessage());
    //         response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    //     };
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
