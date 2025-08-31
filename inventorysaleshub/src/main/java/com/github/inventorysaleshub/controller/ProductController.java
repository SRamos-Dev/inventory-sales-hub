package com.github.inventorysaleshub.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.inventorysaleshub.dto.*;
import com.github.inventorysaleshub.model.*;
import com.github.inventorysaleshub.repository.CategoryRepository;
import com.github.inventorysaleshub.repository.ProductHistoryRepository;
import com.github.inventorysaleshub.repository.ProductRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductHistoryRepository productHistoryRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository,
                             ProductHistoryRepository productHistoryRepository,
                             CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productHistoryRepository = productHistoryRepository;
        this.categoryRepository = categoryRepository;
    }

    // ------------------ CREATE ------------------
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProductDTO>> createProduct(
            @Valid @RequestBody ProductRequestDTO request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product saved = productRepository.save(product);

        // Save history
        ProductHistory history = new ProductHistory();
        history.setProduct(saved);
        history.setAction("CREATED");
        history.setTimestamp(LocalDateTime.now());
        productHistoryRepository.save(history);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Product created successfully", new ProductDTO(saved)));
    }

    // ------------------ READ (by ID) ------------------
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductDTO>> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(
                        new ApiResponseDTO<>(true, "Product found", new ProductDTO(product))
                ))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Product not found", null)));
    }

    // ------------------ UPDATE ------------------
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO request) {

        return productRepository.findById(id).map(product -> {
            // Save update history
            ProductHistory history = new ProductHistory();
            history.setProduct(product);
            history.setAction("UPDATED");
            history.setTimestamp(LocalDateTime.now());
            productHistoryRepository.save(history);

            // Update product
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());

            Product updated = productRepository.save(product);

            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Product updated", new ProductDTO(updated)));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, "Product not found", null)));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Object>> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id).map(product -> {
            // Save delete history
            ProductHistory history = new ProductHistory();
            history.setProduct(product);
            history.setAction("DELETED");
            history.setTimestamp(LocalDateTime.now());
            productHistoryRepository.save(history);

            productRepository.delete(product);

            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Product deleted successfully", null));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, "Product not found", null)));
    }

    // ------------------ GET BY CATEGORY ------------------
    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> getProductsByCategory(@PathVariable Long id) {
        List<ProductDTO> products = productRepository.findByCategoryId(id)
                .stream()
                .map(ProductDTO::new)
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Products by category", products));
    }

    // ------------------ SEARCH ------------------
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> searchProductsByName(@RequestParam String nombre) {
        List<ProductDTO> results = productRepository.findByNameContainingIgnoreCase(nombre)
                .stream()
                .map(ProductDTO::new)
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Search results", results));
    }

    // ------------------ HISTORY ------------------
    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponseDTO<List<ProductHistoryDTO>>> getProductHistory(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    List<ProductHistoryDTO> history = productHistoryRepository.findByProductId(product.getId())
                            .stream()
                            .map(ProductHistoryDTO::new)
                            .toList();
                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Product history", history));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Product not found", null)));
    }

    // ------------------ LOW STOCK ------------------
    @GetMapping("/stock/low")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> getProductsWithLowStock() {
        List<ProductDTO> lowStockProducts = productRepository.findByStockLessThan(5)
                .stream()
                .map(ProductDTO::new)
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Low stock products", lowStockProducts));
    }

    // ------------------ PAGINATION ------------------
    @GetMapping("/page")
    public ResponseEntity<ApiResponseDTO<Page<ProductDTO>>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productsPage = productRepository.findAll(pageable)
                .map(ProductDTO::new);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Paginated products", productsPage));
    }

    // ------------------ SORT ------------------
    @GetMapping("/sort")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> getProductsSorted(
            @RequestParam String por,
            @RequestParam(defaultValue = "true") boolean asc) {

        Sort sort = asc ? Sort.by(por).ascending() : Sort.by(por).descending();
        List<ProductDTO> products = productRepository.findAll(sort)
                .stream()
                .map(ProductDTO::new)
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Sorted products", products));
    }
}
