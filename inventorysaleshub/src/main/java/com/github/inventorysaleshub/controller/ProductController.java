package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.model.Product;
import com.github.inventorysaleshub.model.ProductHistory;
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

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @GetMapping
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());  
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id).map(product -> {

            // History saving before the update
            ProductHistory history = new ProductHistory();
            history.setProduct(product);
            history.setAction("UPDATED");
            history.setTimestamp(LocalDateTime.now());
            productHistoryRepository.save(history);

            // Product update
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setStock(updatedProduct.getStock());
            return ResponseEntity.ok(productRepository.save(product));
        }).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product newProduct) {
        Product savedProduct = productRepository.save(newProduct);

        // Save creation history
        ProductHistory history = new ProductHistory();
        history.setProduct(savedProduct);
        history.setAction("CREATED");
        history.setTimestamp(LocalDateTime.now());
        productHistoryRepository.save(history);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
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
