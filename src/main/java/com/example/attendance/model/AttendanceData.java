

package com.example.attendance.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "attendance_data")
public class AttendanceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Long instituteId; // Changed this to Long to match frontend payload

    private Date loginTime;

    private String loginOption; // e.g., "tea", "lunch", "breakfast", "exit"

    @Column(length = 500) // Ensures remarks can store up to 500 characters
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Long instituteId) { // Changed to Long to match frontend
        this.instituteId = instituteId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginOption() {
        return loginOption;
    }

    public void setLoginOption(String loginOption) {
        this.loginOption = loginOption;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

