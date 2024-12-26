package com.example.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.model.Leave;
import com.example.attendance.repository.LeaveRepository;
import com.example.attendance.repository.UserRepository;

@Service
public class LeaveService {
     @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

  
    public Leave saveLeaveApplication(Leave leave) {
        return leaveRepository.save(leave);
    }
    public Leave getLeaveById(Long id) {
        return leaveRepository.findById(id).orElse(null);
    }

    public List<Leave> getAllLeaveRequests() {
        List<Leave> leaves = leaveRepository.findByStatus("Pending");
        
        // Populate the userName for each leave
        for (Leave leave : leaves) {
            System.out.println(leave.getUserId());
            userRepository.findById(leave.getUserId()).ifPresent(user -> leave.setUserName(user.getName()));
        }

        return leaves;
    }

    // public Leave updateLeaveRequestStatus(Long id, String status) {
    //     Leave leaveRequest = leaveRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Leave request not found"));
    //     leaveRequest.setStatus(status);
    //     return leaveRepository.save(leaveRequest);
    // }
    
    public void updateLeaveStatus(Long leaveId, String status) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        leave.setStatus(status); // Assuming 'status' is a field in the Leave entity
        leaveRepository.save(leave); // Save the updated status
    }

    public long countApprovedLeaves() {
        return leaveRepository.findByStatus("Accepted").size();
    }

}
