package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.ProductDTO;
import com.github.inventorysaleshub.dto.ProductRequestDTO;
import com.github.inventorysaleshub.dto.ProductUpdateDTO;
import com.github.inventorysaleshub.model.Category;
import com.github.inventorysaleshub.model.Product;
import com.github.inventorysaleshub.model.ProductHistory;
import com.github.inventorysaleshub.repository.CategoryRepository;
import com.github.inventorysaleshub.repository.ProductHistoryRepository;
import com.github.inventorysaleshub.repository.ProductRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(
            productRepository.findAll().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList())
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
            .map(product -> ResponseEntity.ok(new ProductDTO(product)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct( @PathVariable Long id, @Valid @RequestBody ProductUpdateDTO request) {

        return productRepository.findById(id).map(product -> {

            // History saving before de update
            ProductHistory history = new ProductHistory();
            history.setProduct(product);
            history.setAction("UPDATED");
            history.setTimestamp(LocalDateTime.now());
            productHistoryRepository.save(history);

            // Product allows update
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());

            Product updated = productRepository.save(product);
            return ResponseEntity.ok(new ProductDTO(updated));

        }).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));


        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id).map(product -> {
            ProductHistory history = new ProductHistory();
            history.setProduct(product);
            history.setAction("DELETED");
            history.setTimestamp(LocalDateTime.now());
            productHistoryRepository.save(history);

            productRepository.delete(product);

            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long id) {
        List<Product> products = productRepository.findByCategoryId(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String nombre) {
        List<Product> results = productRepository.findByNameContainingIgnoreCase(nombre);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ProductHistory>> getProductHistory(@PathVariable Long id) {
        return productRepository.findById(id)
            .map(product -> {
                List<ProductHistory> history = productHistoryRepository.findByProductId(product.getId());
                return ResponseEntity.ok(history);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stock/low")
    public ResponseEntity<List<Product>> getProductsWithLowStock() {
        List<Product> lowStockProducts = productRepository.findByStockLessThan(5);
        return ResponseEntity.ok(lowStockProducts);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Product>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productRepository.findAll(pageable);
        return ResponseEntity.ok(productsPage);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Product>> getProductsSorted(
            @RequestParam String por,
            @RequestParam(defaultValue = "true") boolean asc) {

        Sort sort = asc ? Sort.by(por).ascending() : Sort.by(por).descending();
        List<Product> products = productRepository.findAll(sort);
        return ResponseEntity.ok(products);
    }

}
