package com.example.attendance.controller;

import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;
import com.example.attendance.service.InstituteService;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


 @CrossOrigin(origins = "https://final-qr-code.vercel.app/")
// @CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthController {


    @Autowired
    private InstituteService instituteService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        // Fetch user by email
        Optional<User> existingUserOpt = userRepository.findByEmail(user.getEmail());
        if (existingUserOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        User existingUser = existingUserOpt.get();

        // Check if password matches
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }

        // Generate a mock token (for demonstration purposes)
        String token = "mock-token-for-" + existingUser.getEmail();

        // Prepare the response
        response.put("message", "Login successful");
        response.put("token", token);
        response.put("role", existingUser.getRole()); // Include role in response
        response.put("email", existingUser.getEmail());
        response.put("Id", existingUser.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        // Check if email already exists
        if (userService.emailExists(user.getEmail())) {
            response.put("message", "Email already exists");
            return ResponseEntity.status(409).body(response);
        }

        // Check if admin already exists
        if (user.getRole() == Role.ADMIN) {
            Optional<User> adminOpt = userService.findByRole("ADMIN");
            if (adminOpt.isPresent()) {
                response.put("message", "An admin already exists. Only one admin can be registered.");
                return ResponseEntity.status(400).body(response);
            }
        }

        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole(Role.USER); // Default role
        }

        // Set course only if the role is USER
        if (user.getRole() == Role.USER && user.getCourse() == null) {
            response.put("message", "Course must be specified for users.");
            return ResponseEntity.status(400).body(response);
        } else if (user.getRole() == Role.ADMIN) {
            user.setCourse(null); // Ensure course is null for admins
        }

        // Hash password and save user
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        response.put("message", "Registration successful");
        return ResponseEntity.ok(response);
    }

    @SuppressWarnings("unused")
    @GetMapping("/institute/id")
    public ResponseEntity<?> getInstituteId(@RequestParam String instituteName) {
        Long instituteId = instituteService.getInstituteId(instituteName);
        if(instituteId != null){
            return ResponseEntity.ok(instituteId);
        }else{
            return ResponseEntity.status(404).body("Institute not found");
        }

    }
    
        // Endpoint to check if an admin already exists
    @PostMapping("/check-admin")
    public ResponseEntity<Map<String, Object>> checkAdmin() {
        Map<String, Object> response = new HashMap<>();
        Optional<User> adminOpt = userService.findByRole("ADMIN");
        if (adminOpt.isPresent()) {
            response.put("exists", true);  // Admin exists
        } else {
            response.put("exists", false);  // Admin does not exist
        }
        return ResponseEntity.ok(response);
    }
    
}
