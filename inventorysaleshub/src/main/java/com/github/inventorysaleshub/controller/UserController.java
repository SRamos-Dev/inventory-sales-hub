package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.*;
import com.github.inventorysaleshub.model.Role;
import com.github.inventorysaleshub.model.User;
import com.github.inventorysaleshub.repository.RoleRepository;
import com.github.inventorysaleshub.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    // --- Get all users ---
    @Operation(summary = "Get all users", description = "Retrieve all registered users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(u -> modelMapper.map(u, UserDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Users retrieved successfully", users));
    }

    // --- Get user by ID ---
    @Operation(summary = "Get a user by ID", description = "Retrieve a single user by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(new ApiResponseDTO<>(true, "User retrieved successfully",
                        modelMapper.map(user, UserDTO.class))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "User not found", null)));
    }

    // --- Create a new user ---
    @Operation(summary = "Create a new user", description = "Register a new user with role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserDTO>> createUser(
            @Valid @RequestBody UserRequestDTO request) {

        User user = modelMapper.map(request, User.class);

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + request.getRoleId()));

        user.setRole(role);
        User saved = userRepository.save(user);

        UserDTO response = modelMapper.map(saved, UserDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "User created successfully", response));
    }

    // --- Update a user ---
    @Operation(summary = "Update a user", description = "Update an existing user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO request) {

        return userRepository.findById(id)
                .map(user -> {
                    modelMapper.map(request, user);

                    Role role = roleRepository.findById(request.getRoleId())
                            .orElseThrow(() -> new RuntimeException("Role not found with ID: " + request.getRoleId()));
                    user.setRole(role);

                    User updated = userRepository.save(user);
                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "User updated successfully",
                            modelMapper.map(updated, UserDTO.class)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "User not found", null)));
    }

    // --- Delete a user ---
    @Operation(summary = "Delete a user", description = "Remove a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(true, "User deleted successfully", null);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(false, "User not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}
