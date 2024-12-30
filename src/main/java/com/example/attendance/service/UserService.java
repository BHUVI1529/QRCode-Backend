package com.example.attendance.service;

import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Check if the email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null); // Return the user if found, or null otherwise
    }

    // Find a user by role
    public Optional<User> findByRole(String role) {
       try{
        Role roleEnum = Role.valueOf(role.toUpperCase());
        return userRepository.findByRole(roleEnum);
       }catch (IllegalArgumentException e){
        return Optional.empty();
       }
    }

    // Get all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    

    // Find user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public long countByRoleNot(Role role) {
        return userRepository.countByRoleNot(role);  // Passing Role enum
    }

     public List<User> getAllUsersByRole(String role) {
        try{
         Role roleEnum = Role.valueOf(role.toUpperCase());
         return userRepository.findAllByRole(roleEnum);
        }catch (IllegalArgumentException e){
        
        
		return null;
        }
   }
   // update user by id
   public User updateUser(Long id, User updatedUser) {
    Optional<User> existingUserOpt = userRepository.findById(id);
    if (existingUserOpt.isPresent()) {
        User existingUser = existingUserOpt.get();
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setCourse(updatedUser.getCourse());
        existingUser.setRole(updatedUser.getRole());
        return userRepository.save(existingUser);
    } else {
        return null;  // User not found
    }
}


// Delete a user by ID
public void deleteUser(Long id) {
    if (userRepository.existsById(id)) {
        userRepository.deleteById(id);
    } else {
        throw new RuntimeException("User not found with ID: " + id);
    }
}

public User getUserById(Long userId) {
    return userRepository.findById(userId).orElse(null); // Return null if user not found
}

public Map<String, Long> getCourseDistribution() {
    // Fetch all users from the repository
    List<User> users = userRepository.findAll();

    // Group by course and count the number of users in each course, excluding "Unknown" courses
    return users.stream()
        .filter(user -> user.getCourse() != null && !user.getCourse().equals("Unknown")) // Exclude "Unknown" courses
        .collect(Collectors.groupingBy(
            user -> user.getCourse(), // Group by course name
            Collectors.counting()
            ));
}
}



