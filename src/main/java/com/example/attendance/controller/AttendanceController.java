package com.example.attendance.controller;

import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.Institute;
import com.example.attendance.model.User;
import com.example.attendance.repository.InstituteRepository;
import com.example.attendance.repository.UserRepository;
import com.example.attendance.service.AttendanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstituteRepository instituteRepository;

    @PostMapping("/add")
    public ResponseEntity<String> markAttendance(@RequestBody AttendanceData attendanceData) {
        System.out.println("Attendance Data Received: " + attendanceData);

        // Validate User
        if (attendanceData.getUser() == null || attendanceData.getUser().getId() == null) {
            return ResponseEntity.status(400).body("User data is missing or invalid.");
        }

        Optional<User> userOpt = userRepository.findById(attendanceData.getUser().getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        User user = userOpt.get();

        // Validate Institute
        Long instituteId = attendanceData.getInstituteId();
        if (instituteId == null) {
            return ResponseEntity.status(400).body("Institute ID is required.");
        }

        Optional<Institute> instituteOpt = instituteRepository.findById(instituteId);
        if (instituteOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Institute not found.");
        }

        // Validate Login Option
        String loginOption = attendanceData.getLoginOption();
        if (loginOption == null || loginOption.isEmpty()) {
            return ResponseEntity.status(400).body("Missing required field: loginOption.");
        }

        // Set attendance details
        attendanceData.setUser(user);
        attendanceData.setInstituteId(instituteId);
        attendanceData.setLoginTime(new Date());

        // Handle Logout Case
        if ("logout".equalsIgnoreCase(loginOption)) {
            if (attendanceData.getRemarks() != null && !attendanceData.getRemarks().isEmpty()) {
                attendanceService.saveAttendance(attendanceData, instituteId);
                return ResponseEntity.ok("User logged out successfully and logout data stored.");
            } else {
                return ResponseEntity.status(400).body("Remarks are required for logout.");
            }
        }

        // Save Attendance for Login
        attendanceService.saveAttendance(attendanceData, instituteId);
        return ResponseEntity.ok("Attendance marked successfully.");
    }

    @PostMapping("/determine")
    public ResponseEntity<Map<String, String>> determineNextAction(@RequestBody Map<String, Integer> request) {
        Integer userId = request.get("userId");
        if (userId == null) {
            return ResponseEntity.status(400).body(Map.of("error","User ID is required."));
        }

        // Fetch the latest attendance record for the user for the current date
        AttendanceData latestAttendance = attendanceService.findLatestAttendanceForUserOnDate(userId);

        // Determine the next action
        String nextAction;
        if (latestAttendance == null) {
             nextAction = "login";
        } else {
            String currentAction = latestAttendance.getLoginOption();
            nextAction = "logout".equalsIgnoreCase(currentAction) ? "login" : "logout";
        }
        return ResponseEntity.ok(Map.of("loginOption",nextAction));
    
    }
    
   @GetMapping("/count/present-today")
public ResponseEntity<Map<String, Long>> getPresentToday() {
    long presentCount = attendanceService.countPresentToday();
    Map<String, Long> response = new HashMap<>();
    response.put("presentToday", presentCount);
    return ResponseEntity.ok(response);
}

      @GetMapping("/count/absentees")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<Long> countAbsentees(@RequestParam("date") String date) {
       long absenteesCount = attendanceService.countAbsentees(date);
       return ResponseEntity.ok(absenteesCount);
   }
}
