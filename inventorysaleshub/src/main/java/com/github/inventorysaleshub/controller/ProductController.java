package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.model.Product;
import com.github.inventorysaleshub.model.ProductHistory;
import com.github.inventorysaleshub.repository.ProductHistoryRepository;
import com.github.inventorysaleshub.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            history.setAction("Update");
            history.setTimestamp(LocalDateTime.now());
            productHistoryRepository.save(history);

            // Product update
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setStock(updatedProduct.getStock());
            return ResponseEntity.ok(productRepository.save(product));
        }).orElse(ResponseEntity.notFound().build());
    }
}
