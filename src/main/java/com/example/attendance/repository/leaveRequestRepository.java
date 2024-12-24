package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.attendance.model.leaveRequest;

@Repository
public interface leaveRequestRepository extends JpaRepository<leaveRequest, Long> {
}
