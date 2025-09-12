package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.ApiResponseDTO;
import com.github.inventorysaleshub.dto.SupplierDTO;
import com.github.inventorysaleshub.dto.SupplierRequestDTO;
import com.github.inventorysaleshub.model.Supplier;
import com.github.inventorysaleshub.repository.SupplierRepository;
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

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    public SupplierController(SupplierRepository supplierRepository, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
    }

    // --- Get all suppliers ---
    @Operation(summary = "Get all suppliers", description = "Retrieve all suppliers")
    @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDTO<List<SupplierDTO>>> getAllSuppliers() {
        List<SupplierDTO> suppliers = supplierRepository.findAll()
                .stream()
                .map(s -> modelMapper.map(s, SupplierDTO.class))
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Suppliers retrieved successfully", suppliers));
    }

    // --- Get supplier by ID ---
    @Operation(summary = "Get a supplier by ID", description = "Retrieve a single supplier by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDTO<SupplierDTO>> getSupplierById(@PathVariable Long id) {
        return supplierRepository.findById(id)
                .map(supplier -> ResponseEntity.ok(new ApiResponseDTO<>(true, "Supplier retrieved successfully",
                        modelMapper.map(supplier, SupplierDTO.class))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Supplier not found", null)));
    }

    // --- Create a new supplier ---
    @Operation(summary = "Create a new supplier", description = "Register a new supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<SupplierDTO>> createSupplier(@Valid @RequestBody SupplierRequestDTO request) {
        Supplier supplier = modelMapper.map(request, Supplier.class);
        Supplier saved = supplierRepository.save(supplier);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Supplier created successfully",
                        modelMapper.map(saved, SupplierDTO.class)));
    }

    // --- Update a supplier ---
    @Operation(summary = "Update a supplier", description = "Update an existing supplier by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<SupplierDTO>> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO request) {

        return supplierRepository.findById(id)
                .map(supplier -> {
                    modelMapper.map(request, supplier);
                    Supplier updated = supplierRepository.save(supplier);

                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Supplier updated successfully",
                            modelMapper.map(updated, SupplierDTO.class)));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Supplier not found", null)));
    }

    // --- Delete a supplier ---
    @Operation(summary = "Delete a supplier", description = "Remove a supplier by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteSupplier(@PathVariable Long id) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    supplierRepository.delete(supplier);
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(true, "Supplier deleted successfully", null);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(false, "Supplier not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}

