package com.example.attendance.service;

import com.example.attendance.model.leaveRequest;
import com.example.attendance.repository.leaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestService {
    @Autowired
    private leaveRequestRepository leaveRequestRepository;

    public void approveLeave(Long id) {
        leaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        leave.setApproved(true);
        leaveRequestRepository.save(leave);
    }

    public List<leaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }
}

