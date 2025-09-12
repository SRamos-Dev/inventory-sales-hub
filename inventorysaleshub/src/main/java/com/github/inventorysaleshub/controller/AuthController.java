package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.*;
import com.github.inventorysaleshub.model.Role;
import com.github.inventorysaleshub.model.User;
import com.github.inventorysaleshub.repository.RoleRepository;
import com.github.inventorysaleshub.repository.UserRepository;
import com.github.inventorysaleshub.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;

    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider,
                          ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.modelMapper = modelMapper;
    }

    // --- User login ---
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    @PermitAll // Public endpoint (no authentication required)
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO<>(false, "Invalid email or password", null));
        }

        User user = userOpt.get();
        String token = jwtProvider.generateToken(user.getEmail(), user.getRole().getName());

        // Build response DTO
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUserName(user.getName());
        response.setUserEmail(user.getEmail());
        response.setRoleName(user.getRole().getName());

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Login successful", response));
    }

    // --- User registration ---
    @Operation(summary = "User registration", description = "Register a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or email already taken")
    })
    @PostMapping("/register")
    @PermitAll // Public endpoint (no authentication required)
    public ResponseEntity<ApiResponseDTO<UserDTO>> register(@Valid @RequestBody RegisterDTO request) {
        // Check if email is already registered
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, "Email is already in use", null));
        }

        // Retrieve role
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + request.getRoleId()));

        // Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password
        user.setRole(role);

        // Save in DB
        User saved = userRepository.save(user);

        // Map to DTO
        UserDTO dto = modelMapper.map(saved, UserDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "User registered successfully", dto));
    }
}
