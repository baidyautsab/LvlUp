package com.LvlUp.Controller;

import com.LvlUp.Entity.User;
import com.LvlUp.Repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
