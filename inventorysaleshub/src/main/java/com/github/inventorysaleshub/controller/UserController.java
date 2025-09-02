package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.ApiResponseDTO;
import com.github.inventorysaleshub.dto.UserDTO;
import com.github.inventorysaleshub.dto.UserRequestDTO;
import com.github.inventorysaleshub.model.Role;
import com.github.inventorysaleshub.model.User;
import com.github.inventorysaleshub.repository.RoleRepository;
import com.github.inventorysaleshub.repository.UserRepository;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // ------------------ CREATE ------------------
    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserDTO>> createUser(@Valid @RequestBody UserRequestDTO request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + request.getRoleId()));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(role);

        User saved = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "User created successfully", new UserDTO(saved)));
    }

    // ------------------ READ (by ID) ------------------
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(
                        new ApiResponseDTO<>(true, "User found", new UserDTO(user))
                ))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "User not found", null)));
    }

    // ------------------ LIST ALL ------------------
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "All users retrieved", users));
    }

    // ------------------ UPDATE ------------------
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO request) {

        return userRepository.findById(id).map(user -> {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + request.getRoleId()));

            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setRole(role);

            User updated = userRepository.save(user);

            return ResponseEntity.ok(new ApiResponseDTO<>(true, "User updated successfully", new UserDTO(updated)));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, "User not found", null)));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Object>> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "User deleted successfully", null));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, "User not found", null)));
    }
}


