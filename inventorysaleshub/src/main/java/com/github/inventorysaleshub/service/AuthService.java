package com.github.inventorysaleshub.service;

import com.github.inventorysaleshub.dto.*;
import com.github.inventorysaleshub.model.Role;
import com.github.inventorysaleshub.model.User;
import com.github.inventorysaleshub.repository.RoleRepository;
import com.github.inventorysaleshub.repository.UserRepository;
import com.github.inventorysaleshub.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    // --- Register new user ---
    public UserDTO register(RegisterDTO request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + request.getRoleId()));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password

        User saved = userRepository.save(user);

        return new UserDTO(saved);
    }

    // --- Login user ---
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate token
        String token = jwtProvider.generateToken(user.getEmail(), user.getRole().getName());

        // Build response DTO
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUserName(user.getName());
        response.setUserEmail(user.getEmail());
        response.setRoleName(user.getRole().getName());

        return response;
    }
}

