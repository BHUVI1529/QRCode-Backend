package com.example.attendance.repository;

import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByRole(Role role); // Finds users by role

    boolean existsByEmail(String email); // Check if email exists

    Optional<User> findByEmail(String email);
    long countByRoleNot(Role role);

    // @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllByRole(Role role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role != 'ADMIN'")
   long countNonAdminUsers();

}



