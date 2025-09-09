package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.*;
import com.github.inventorysaleshub.model.Product;
import com.github.inventorysaleshub.model.ProductHistory;
import com.github.inventorysaleshub.repository.ProductRepository;
import com.github.inventorysaleshub.repository.ProductHistoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductHistoryRepository productHistoryRepository;
    private final ModelMapper modelMapper;

    public ProductController(ProductRepository productRepository,
                             ProductHistoryRepository productHistoryRepository,
                             ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.productHistoryRepository = productHistoryRepository;
        this.modelMapper = modelMapper;
    }

    // --- Get all products ---
    @Operation(summary = "Get all products", description = "Retrieve all products from inventory")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Products retrieved successfully", products));
    }

    // --- Get product by ID ---
    @Operation(summary = "Get a product by ID", description = "Retrieve a single product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductDTO>> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(new ApiResponseDTO<>(true, "Product retrieved successfully",
                        modelMapper.map(product, ProductDTO.class))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Product not found", null)));
    }

    // --- Create a new product ---
    @Operation(summary = "Create a new product", description = "Register a new product in the inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProductDTO>> createProduct(
            @Valid @RequestBody ProductRequestDTO request) {

        Product product = modelMapper.map(request, Product.class);
        Product saved = productRepository.save(product);

        ProductHistory history = new ProductHistory();
        history.setProduct(saved);
        history.setAction("CREATED");
        history.setTimestamp(LocalDateTime.now());
        productHistoryRepository.save(history);

        ProductDTO response = modelMapper.map(saved, ProductDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Product created successfully", response));
    }

    // --- Update a product ---
    @Operation(summary = "Update a product", description = "Update an existing product by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request) {

        return productRepository.findById(id)
                .map(product -> {
                    modelMapper.map(request, product);

                    ProductHistory history = new ProductHistory();
                    history.setProduct(product);
                    history.setAction("UPDATED");
                    history.setTimestamp(LocalDateTime.now());
                    productHistoryRepository.save(history);

                    Product updated = productRepository.save(product);
                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Product updated successfully",
                            modelMapper.map(updated, ProductDTO.class)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Product not found", null)));
    }

    // --- Delete a product ---
    @Operation(summary = "Delete a product", description = "Remove a product by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(true, "Product deleted successfully", null);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(false, "Product not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }


    // --- Get products by category ---
    @Operation(summary = "Get products by category", description = "Retrieve products that belong to a specific category")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> getProductsByCategory(@PathVariable Long id) {
        List<ProductDTO> products = productRepository.findByCategoryId(id)
                .stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Products retrieved successfully", products));
    }

    // --- Search products by name ---
    @Operation(summary = "Search products", description = "Find products by name (case insensitive)")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> searchProductsByName(@RequestParam String name) {
        List<ProductDTO> results = productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Products retrieved successfully", results));
    }

    // --- Get product history ---
    @Operation(summary = "Get product history", description = "Retrieve the change history of a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product history retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponseDTO<List<ProductHistory>>> getProductHistory(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    List<ProductHistory> history = productHistoryRepository.findByProductId(product.getId());
                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Product history retrieved successfully", history));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Product not found", null)));
    }

    // --- Get products with low stock ---
    @Operation(summary = "Get products with low stock", description = "Retrieve products with stock lower than 5")
    @ApiResponse(responseCode = "200", description = "Low stock products retrieved successfully")
    @GetMapping("/stock/low")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> getProductsWithLowStock() {
        List<ProductDTO> lowStockProducts = productRepository.findByStockLessThan(5)
                .stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Low stock products retrieved successfully", lowStockProducts));
    }

    // --- Get products with pagination ---
    @Operation(summary = "Get products with pagination", description = "Retrieve products with page and size parameters")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/page")
    public ResponseEntity<ApiResponseDTO<Page<ProductDTO>>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productsPage = productRepository.findAll(pageable)
                .map(p -> modelMapper.map(p, ProductDTO.class));
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Products retrieved successfully", productsPage));
    }

    // --- Get products sorted ---
    @Operation(summary = "Get products sorted", description = "Retrieve products sorted by a field and order")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/sort")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> getProductsSorted(
            @RequestParam String by,
            @RequestParam(defaultValue = "true") boolean asc) {

        Sort sort = asc ? Sort.by(by).ascending() : Sort.by(by).descending();
        List<ProductDTO> products = productRepository.findAll(sort)
                .stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Products retrieved successfully", products));
    }
}
