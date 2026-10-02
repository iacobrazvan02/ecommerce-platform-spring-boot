package com.electrotech.store.controller;

import com.electrotech.store.model.ProductImage;
import com.electrotech.store.model.Product;
import com.electrotech.store.repository.ProductImageRepository;
import com.electrotech.store.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    public ProductImageController(ProductImageRepository productImageRepository,
                                  ProductRepository productRepository) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/product/{productId}")
    public List<ProductImage> getImagesForProduct(@PathVariable @NonNull Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    @PostMapping
    public ResponseEntity<?> addImage(@RequestBody Map<String, Object> data) {
        try {
            Long productId = Long.parseLong(data.get("productId").toString());
            String imageUrl = data.get("imageUrl").toString();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produs negăsit"));

            ProductImage img = new ProductImage();
            img.setProduct(product);
            img.setImageUrl(imageUrl);

            ProductImage saved = productImageRepository.save(img);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable @NonNull Integer id) {
        try {
            productImageRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Imagine ștearsă"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
