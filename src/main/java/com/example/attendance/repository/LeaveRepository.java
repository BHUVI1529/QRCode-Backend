package com.example.attendance.repository;

import com.example.attendance.model.Leave;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long>{

    List<Leave> findByStatus(String status);
}
