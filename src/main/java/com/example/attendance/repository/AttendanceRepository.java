package com.example.attendance.repository;

import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.User;

import java.util.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<AttendanceData, Long> {

    // boolean existsByUserAndLoginOptionAndLoginTimeAfter(User user, String loginOption, Date startOfDay);
    
    
    // DP UPDATE BELOW CODE 
    @Query("SELECT a FROM AttendanceData a WHERE a.user.id = :userId AND DATE(a.loginTime) = :date")
    List<AttendanceData> findByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    

      // Use SQL DATE function to filter records by date
   @Query("SELECT a FROM AttendanceData a WHERE DATE(a.loginTime) = CURRENT_DATE")
   List<AttendanceData> findAttendanceForCurrentDate();
   
 @Query("SELECT a FROM AttendanceData a WHERE a.user.id = :userId " +
                        "AND DATE(a.loginTime) = :date ORDER BY a.loginTime DESC limit 1")
        AttendanceData findTopByUserIdAndLoginDateOrderByLoginTimeDesc(
                        @Param("userId") Integer userId,

                        @Param("date") LocalDate date);

        List<AttendanceData> findByUserId(Long id);

        boolean existsByUserAndLoginOptionAndLoginTimeAfterAndInstituteId(User user, String loginOption,
                        Date startOfDay,
                        Long instituteId);

        boolean existsByUserAndLoginOptionAndLoginTimeAfter(User user, String loginOption, Date startOfDay);

        long countByLoginTimeAfter(Date startOfDay);
         // Count distinct users who marked login today
        //long countDistinctByLoginTimeAfterAndLoginOption(Date startOfDay, String loginOption);

          // Count distinct users who marked login attendance today
        //  @Query("SELECT DISTINCT a.user.id FROM AttendanceData a WHERE a.loginOption = :loginOption " +
          //"AND a.loginTime > :startOfDay")
          @Query("SELECT COUNT(DISTINCT a.user.id) FROM AttendanceData a WHERE a.loginTime >= :startOfDay AND a.loginOption = :loginOption AND a.user.id IS NOT NULL")
          long countDistinctByLoginTimeAfterAndLoginOptionAndUserIdIsNotNull(@Param("startOfDay") Date startOfDay, @Param("loginOption") String loginOption);

         // long countDistinctByLoginTimeAfterAndLoginOptionAndStatusAndUserIdIsNotNull(Date startOfDay, String loginOption, String status);

    // Modified query to find attendance for a given date, ignoring time range
              @Query("SELECT DISTINCT a.user.id FROM AttendanceData a WHERE FUNCTION('DATE', a.loginTime) = :attendanceDate")
              List<Long> findPresentUserIdsByDate(@Param("attendanceDate") LocalDate attendanceDate);

              @Query("SELECT a FROM AttendanceData a WHERE FUNCTION('DATE', a.loginTime) = :attendanceDate")
    List<AttendanceData> findAttendanceRecordsByDate(@Param("attendanceDate") LocalDate attendanceDate);
    
}
    
