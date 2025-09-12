package com.github.inventorysaleshub.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.github.inventorysaleshub.dto.ApiResponseDTO;
import com.github.inventorysaleshub.dto.RoleDTO;
import com.github.inventorysaleshub.dto.RoleRequestDTO;
import com.github.inventorysaleshub.model.Role;
import com.github.inventorysaleshub.repository.RoleRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleController(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    // --- Get all roles ---
    @Operation(summary = "Get all roles", description = "Retrieve all roles")
    @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<RoleDTO>>> getAllRoles() {
        List<RoleDTO> roles = roleRepository.findAll()
                .stream()
                .map(r -> modelMapper.map(r, RoleDTO.class))
                .toList();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Roles retrieved successfully", roles));
    }

    // --- Get role by ID ---
    @Operation(summary = "Get a role by ID", description = "Retrieve a single role by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<RoleDTO>> getRoleById(@PathVariable Long id) {
        return roleRepository.findById(id)
                .map(role -> ResponseEntity.ok(new ApiResponseDTO<>(true, "Role retrieved successfully",
                        modelMapper.map(role, RoleDTO.class))))
                .orElseGet(() -> {
                    ApiResponseDTO<RoleDTO> response = new ApiResponseDTO<>(false, "Role not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    // --- Create a new role ---
    @Operation(summary = "Create a new role", description = "Register a new role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Role created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<RoleDTO>> createRole(
            @Valid @RequestBody RoleRequestDTO request) {

        Role role = modelMapper.map(request, Role.class);
        Role saved = roleRepository.save(role);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Role created successfully",
                        modelMapper.map(saved, RoleDTO.class)));
    }

    // --- Update a role ---
    @Operation(summary = "Update a role", description = "Update an existing role by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role updated successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<RoleDTO>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO request) {

        return roleRepository.findById(id)
                .map(role -> {
                    modelMapper.map(request, role);
                    Role updated = roleRepository.save(role);
                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Role updated successfully",
                            modelMapper.map(updated, RoleDTO.class)));
                })
                .orElseGet(() -> {
                    ApiResponseDTO<RoleDTO> response = new ApiResponseDTO<>(false, "Role not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    // --- Delete a role ---
    @Operation(summary = "Delete a role", description = "Remove a role by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteRole(@PathVariable Long id) {
        return roleRepository.findById(id)
                .map(role -> {
                    roleRepository.delete(role);
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(true, "Role deleted successfully", null);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(false, "Role not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}



