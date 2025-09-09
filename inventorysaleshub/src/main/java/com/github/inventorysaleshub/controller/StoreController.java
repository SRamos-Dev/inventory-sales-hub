package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.ApiResponseDTO;
import com.github.inventorysaleshub.dto.StoreDTO;
import com.github.inventorysaleshub.dto.StoreRequestDTO;
import com.github.inventorysaleshub.model.Store;
import com.github.inventorysaleshub.repository.StoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;

    public StoreController(StoreRepository storeRepository, ModelMapper modelMapper) {
        this.storeRepository = storeRepository;
        this.modelMapper = modelMapper;
    }

    // --- Get all stores ---
    @Operation(summary = "Get all stores", description = "Retrieve all stores")
    @ApiResponse(responseCode = "200", description = "Stores retrieved successfully")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<StoreDTO>>> getAllStores() {
        List<StoreDTO> stores = storeRepository.findAll()
                .stream()
                .map(s -> modelMapper.map(s, StoreDTO.class))
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Stores retrieved successfully", stores));
    }

    // --- Get store by ID ---
    @Operation(summary = "Get a store by ID", description = "Retrieve a single store by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<StoreDTO>> getStoreById(@PathVariable Long id) {
        return storeRepository.findById(id)
                .map(store -> ResponseEntity.ok(new ApiResponseDTO<>(true, "Store retrieved successfully",
                        modelMapper.map(store, StoreDTO.class))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Store not found", null)));
    }

    // --- Create a new store ---
    @Operation(summary = "Create a new store", description = "Register a new store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Store created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<StoreDTO>> createStore(@Valid @RequestBody StoreRequestDTO request) {
        // Map request to entity
        Store store = modelMapper.map(request, Store.class);

        // Persist entity
        Store saved = storeRepository.save(store);

        // Map to response DTO
        StoreDTO dto = modelMapper.map(saved, StoreDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Store created successfully", dto));
    }

    // --- Update a store ---
    @Operation(summary = "Update a store", description = "Update an existing store by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store updated successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<StoreDTO>> updateStore(
            @PathVariable Long id,
            @Valid @RequestBody StoreRequestDTO request) {

        return storeRepository.findById(id)
                .map(store -> {
                    // Map incoming fields onto the existing entity
                    modelMapper.map(request, store);

                    Store updated = storeRepository.save(store);
                    StoreDTO dto = modelMapper.map(updated, StoreDTO.class);

                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Store updated successfully", dto));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Store not found", null)));
    }

    // --- Delete a store ---
    @Operation(summary = "Delete a store", description = "Remove a store by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteStore(@PathVariable Long id) {
        return storeRepository.findById(id)
                .map(store -> {
                    storeRepository.delete(store);
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(true, "Store deleted successfully", null);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(false, "Store not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}


