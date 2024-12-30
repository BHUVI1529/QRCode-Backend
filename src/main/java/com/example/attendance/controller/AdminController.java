

package com.example.attendance.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
//import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.Leave;
import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.service.LeaveService;
import com.example.attendance.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;

    
    public AdminController(UserService userService, AttendanceService attendanceService, LeaveService leaveService) {
        this.userService = userService;
        this.attendanceService = attendanceService;
        this.leaveService = leaveService;
        
    }

    @GetMapping("/attendance/duration")
    public ResponseEntity<String> getUserDuration( @RequestParam Long userId, @RequestParam String date){
       // Convert String to LocalDate
       LocalDate localDate = LocalDate.parse(date);

      
        // Call the method
        String duration = attendanceService.calculateDuration(userId, localDate);
        return ResponseEntity.ok(duration);
    }

    @GetMapping("/attendance/today")
    public List<AttendanceData> getTodayAttendance() {
        return attendanceService.getAttendanceForCurrentDate();
    }

    // Endpoint to get all users (filter out admins)
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers().stream()
                                      .filter(user -> user.getRole() != Role.ADMIN) // Filter out admins
                                      .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // Endpoint to get a specific user by email
    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Fetch all attendance records
    @GetMapping("/attendance/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceData>> getAllAttendanceRecords() {
        List<AttendanceData> records = attendanceService.findAllAttendanceRecords();
        return ResponseEntity.ok(records);
    }

    // Fetch attendance records for a specific user
    @GetMapping("/attendance/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceData>> getUserAttendanceRecords(@PathVariable Long id) {
        List<AttendanceData> records = attendanceService.findAttendanceRecordsByUserId(id);
        if (records.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(records); 
    }

    // Fetch absentees for a specific date
    @GetMapping("/absentees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAbsentees(@RequestParam("date") String date) {
        List<User> absentees = attendanceService.findAbsentees(date);
        return ResponseEntity.ok(absentees);
    }

    // Fetch all leave requests
    // @GetMapping("/leaves")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<List<leaveRequest>> getAllLeaveRequests() {
    //     List<leaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequests();
    //     return ResponseEntity.ok(leaveRequests);
    // }

    @GetMapping("/users/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getUserCountExcludingAdmin() {
        // Pass Role.ADMIN as the argument (enum)
        long userCount = userService.countByRoleNot(Role.ADMIN);
        return ResponseEntity.ok(new Object() { public long total = userCount; });
    }

     // Fetch all leave requests
    // Endpoint to fetch all leave requests for the admin
   
   

    @GetMapping("/leaveRequests")
    public ResponseEntity<List<Leave>> getAllLeaveRequests() {
        List<Leave> leaveRequests = leaveService.getAllLeaveRequests();
        return ResponseEntity.ok(leaveRequests);
    }

    @GetMapping("leaveRequests/{id}/approve")
    public ResponseEntity<String> acceptLeave(@PathVariable Long id) {
        try {
            leaveService.updateLeaveStatus(id, "Accepted"); // Update leave status
            // emailService.sendLeaveStatusEmail(id, "Accepted"); // Send email to the user
            return ResponseEntity.ok("Leave request accepted and email sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the leave request: " + e.getMessage());
        }
    }

@GetMapping("leaveRequests/{id}/reject")
    public ResponseEntity<String> rejectLeave(@PathVariable Long id) {
        try {
            leaveService.updateLeaveStatus(id, "Rejected"); // Update leave status
            //emailService.sendLeaveStatusEmail(id, "Rejected"); // Send email to the user
            return ResponseEntity.ok("Leave request rejected and email sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the leave request: " + e.getMessage());
        }
    }

    @GetMapping("/attendance/date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceData>> getAttendanceByDate(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        System.out.println("Received Date: " + date);  // Log the incoming date
        List<AttendanceData> records = attendanceService.findAttendanceByDate(date);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/course-distribution")
    @PreAuthorize("hasRole('ADMIN')") // Only accessible by admins
    public ResponseEntity<Map<String, Long>> getCourseDistribution() {
        // Delegate the business logic to the service layer
        Map<String, Long> courseDistribution = userService.getCourseDistribution();
        return ResponseEntity.ok(courseDistribution);
    }

}
