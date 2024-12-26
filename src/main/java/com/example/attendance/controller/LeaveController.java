package com.example.attendance.controller;

import com.example.attendance.model.Leave;
import com.example.attendance.model.User;
//import com.example.attendance.service.EmailService;
import com.example.attendance.service.LeaveService;
import com.example.attendance.service.UserService;
import com.example.attendance.repository.LeaveRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private UserService userService; // Service to fetch user by ID

    @Autowired
    private LeaveRepository leaveRepository; // Repository to interact with the database

    

    @PostMapping
    public ResponseEntity<String> applyForLeave(@RequestBody Leave leave) {
        try {
            if (leave.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is missing.");
            }

            // Fetch the user by ID
            User user = userService.findById(leave.getUserId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user.");
            }

            // Associate the user with the leave application
            leave.setUser(user);

            // Save the leave application
            leaveService.saveLeaveApplication(leave);

            return ResponseEntity.ok("Leave application submitted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to submit leave application: " + e.getMessage());
        }
    }

    // @Autowired
    // private EmailService emailService; // Service for sending emails
    @GetMapping("/on-leave-count")
    public ResponseEntity<Long> getOnLeaveCount() {
        long count = leaveService.countApprovedLeaves();
        return ResponseEntity.ok(count);
    }

}
