package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance.model.Institute;

public interface InstituteRepository extends JpaRepository<Institute, Long> {
    Institute findByInstituteName(String instituteName);
}
