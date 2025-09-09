package com.github.inventorysaleshub.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.inventorysaleshub.dto.ApiResponseDTO;
import com.github.inventorysaleshub.dto.CategoryDTO;
import com.github.inventorysaleshub.dto.CategoryRequestDTO;
import com.github.inventorysaleshub.model.Category;
import com.github.inventorysaleshub.repository.CategoryRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    // --- Get all categories ---
    @Operation(summary = "Get all categories", description = "Retrieve all categories")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .toList();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Categories retrieved successfully", categories));
    }

    // --- Get category by ID ---
    @Operation(summary = "Get a category by ID", description = "Retrieve a single category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok(new ApiResponseDTO<>(true, "Category retrieved successfully",
                        modelMapper.map(category, CategoryDTO.class))))
                .orElseGet(() -> {
                    ApiResponseDTO<CategoryDTO> response = new ApiResponseDTO<>(false, "Category not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    // --- Create a new category ---
    @Operation(summary = "Create a new category", description = "Register a new category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<CategoryDTO>> createCategory(
            @Valid @RequestBody CategoryRequestDTO request) {

        Category category = modelMapper.map(request, Category.class);
        Category saved = categoryRepository.save(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Category created successfully",
                        modelMapper.map(saved, CategoryDTO.class)));
    }

    // --- Update a category ---
    @Operation(summary = "Update a category", description = "Update an existing category by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO request) {

        return categoryRepository.findById(id)
                .map(category -> {
                    modelMapper.map(request, category);
                    Category updated = categoryRepository.save(category);
                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Category updated successfully",
                            modelMapper.map(updated, CategoryDTO.class)));
                })
                .orElseGet(() -> {
                    ApiResponseDTO<CategoryDTO> response = new ApiResponseDTO<>(false, "Category not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    // --- Delete a category ---
    @Operation(summary = "Delete a category", description = "Remove a category by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(true, "Category deleted successfully", null);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(false, "Category not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}

