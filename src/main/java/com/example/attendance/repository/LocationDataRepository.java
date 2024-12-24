package com.example.attendance.repository;

import com.example.attendance.model.LocationData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDataRepository extends JpaRepository<LocationData, Long> {
    // Custom queries can be added here if needed
}
