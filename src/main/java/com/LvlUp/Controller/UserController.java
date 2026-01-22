package com.LvlUp.Controller;

import com.LvlUp.Dto.LoginRequest;
import com.LvlUp.Entity.User;
import com.LvlUp.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        // Successful login
        return ResponseEntity.ok("Login successful! Role: " + user.getRole());
    }

    // 🔹 Get all users (ADMIN use-case)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 🔹 Get user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }
}
