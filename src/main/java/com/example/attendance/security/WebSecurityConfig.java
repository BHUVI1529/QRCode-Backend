package com.example.attendance.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for development
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configure CORS
                .authorizeRequests(auth -> auth
                        // Publicly accessible endpoints
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/attendance/add" , "/api/attendance/determine").permitAll()
                        .requestMatchers("/api/attendance/count/present-today").permitAll()
                        .requestMatchers("/api/check-admin", "/error").permitAll()
                        .requestMatchers("api/institute/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/leave", "/{id}/approve", "/{id}/reject", "/api/email/send-leave-status").permitAll()

                        // Admin endpoints (restricted to ROLE_ADMIN)
                        //.requestMatchers(HttpMethod.GET, "/admin/users","/admin/user/", "/admin/attendance/all","/admin/attendance/date", "/admin/absentees", "/admin/leaveRequests/", "/leave/user/{userId}","/admin/users/count","/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/users","/admin/user/*", "/admin/attendance/all", "/admin/attendance/date" ,"/admin/attendance/today" ,"/admin/attendance/duration" , "/admin/absentees", "/admin/leaveRequests/**", "/leave/user/{userId}","/admin/users/count","/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/attendance/user/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/leave/on-leave-count").permitAll() // No authentication required
                        //.requestMatchers(HttpMethod.PUT, "/admin/leave/approve/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/admin/user/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/admin/user/{id}").permitAll()
                        .requestMatchers("/api/attendance/count/absentees").permitAll()
                        // Default rule: authenticate all other requests
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions (no cookies/session ids)
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Log authentication failure
                            System.out.println("Authentication failed: " + authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                    "Unauthorized: " + authException.getMessage());
                        }));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("https://final-qr-code.vercel.app/")); 
       // config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true); // Allow credentials (cookies, etc.)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

