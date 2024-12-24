package com.example.attendance.service;

import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.User;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.UserRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    public void saveAttendance(AttendanceData attendanceData, Long instituteId) {
         attendanceData.setInstituteId(instituteId);
        attendanceRepository.save(attendanceData);
    }

    public List<AttendanceData> getAttendanceForCurrentDate(){
        return attendanceRepository.findAttendanceForCurrentDate();
    }
    public boolean hasMarkedAttendanceToday(User user, String loginOption) {
        // Assume this method checks if the user has already marked lunch or tea
        // attendance today
        // Implement logic to query the AttendanceRepository to verify if lunch or tea
        // has been marked today
        Date startOfDay = getStartOfDay(); // Helper method to get start of today
        return attendanceRepository.existsByUserAndLoginOptionAndLoginTimeAfter(user, loginOption, startOfDay);
    }

    private Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Date(calendar.getTimeInMillis());
        // Helper method to return the start of the day timestamp
        // Implement this based on your specific date handling requirements
    }

    public List<AttendanceData> findAllAttendanceRecords() {
        // TODO Auto-generated method stub
        return attendanceRepository.findAll();
    }

    public List<AttendanceData> findAttendanceRecordsByUserId(Long id) {
        // TODO Auto-generated method stub
        return attendanceRepository.findByUserId(id);

    }

    // public List<User> findAbsentees(String date) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findAbsentees'");
    // }

    // public List<User> findAbsenteesExcludingAdmins(String date) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findAbsenteesExcludingAdmins'");
    // }

    public List<User> findAbsentees(String date) {
            // Parse the date to LocalDate
            LocalDate attendanceDate = LocalDate.parse(date);

            // Fetch all users with role 'USER'
           List<User> allUsers = userService.getAllUsersByRole("USER");
            


            // Fetch user IDs who marked attendance on the given date (without using time range)
            List<Long> presentUserIds = attendanceRepository.findPresentUserIdsByDate(attendanceDate);

            // Filter out users who are absent
            return allUsers.stream()
                    .filter(user -> !presentUserIds.contains(user.getId()))
                    .collect(Collectors.toList());
        }

     public AttendanceData findLatestAttendanceForUserOnDate(Integer userId) {
        // Get today's date without time
        LocalDate today = LocalDate.now();

        // Fetch the latest attendance record for the given user, institute, and today's
        // date
        return attendanceRepository.findTopByUserIdAndLoginDateOrderByLoginTimeDesc(userId, today);
    }
    public long countPresentToday() {
        // Get start of the day (00:00:00 of today)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
            
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        Date startOfDay = Date.valueOf(today);

        

         // Log the startOfDay value to check if it matches the expected date
    System.out.println("Start of today: " + startOfDay);

        // Query the database for distinct users who logged in today
    long count = attendanceRepository.countDistinctByLoginTimeAfterAndLoginOptionAndUserIdIsNotNull(startOfDay, "login");
     
    // Log the count result
    System.out.println("Count of distinct users who logged in today: " + count);
    
        return count;
     }

     public long countAbsentees(String date) {
        LocalDate attendanceDate = LocalDate.parse(date);
        List<Long> presentUserIds = attendanceRepository.findPresentUserIdsByDate(attendanceDate);

        // Fetch all users and count those who are absent
        long totalUsers = userRepository.countNonAdminUsers();
        long absenteesCount = totalUsers - presentUserIds.size();

        return absenteesCount;
    }

    public List<AttendanceData> findAttendanceByDate(LocalDate date) {
        return attendanceRepository.findAttendanceRecordsByDate(date);
    }
    
}
