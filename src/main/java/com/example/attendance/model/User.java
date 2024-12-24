package com.example.attendance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)  // Changed from unique to non-unique for 'name'
    private String name;  // Renamed from username to name

    @Column(nullable = true)  // New field 'course'
    private String course;  // Added new 'course' field

    @Column(nullable = false, unique = true)  // Added phoneNumber column
    private String phoneNumber;  // New 'phoneNumber' field

    //@OneToMany(mappedBy = "user")

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference  // This prevents recursion from the User side
    private List<Leave> leaves;  // Reverse mapping to retrieve leaves for a user
    
    // Constructors
    public User() {
    }

    public User(String email, String password, Role role, String name, String course, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.course = course;
        this.phoneNumber = phoneNumber;
    }

    public List<Leave> getLeaves() {  // Getter for the leaves
        return leaves;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setLeaves(List<Leave> leaves) {
        this.leaves = leaves;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}