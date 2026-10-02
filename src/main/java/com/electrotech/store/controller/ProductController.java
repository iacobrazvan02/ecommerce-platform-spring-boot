package com.electrotech.store.controller;

import com.electrotech.store.model.Product;
import com.electrotech.store.model.Category;
import com.electrotech.store.model.PriceHistory;
import com.electrotech.store.repository.ProductRepository;
import com.electrotech.store.repository.CategoryRepository;
import com.electrotech.store.repository.PriceHistoryRepository;
import com.electrotech.store.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public ProductController(ProductRepository productRepository, ProductService productService,
                             CategoryRepository categoryRepository,
                             PriceHistoryRepository priceHistoryRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable @NonNull Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam("q") String query) {
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }
        return productRepository.findByNameContainingIgnoreCase(query.trim())
                .stream().limit(6).toList();
    }

    @GetMapping("/predict-2026")
    public ResponseEntity<String> getPrediction() {
        return ResponseEntity.ok(productService.getAIPrediction());
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Map<String, Object> data) {
        try {
            Product product = new Product();
            product.setName((String) data.get("name"));
            product.setBrand((String) data.get("brand"));
            product.setPrice(new BigDecimal(data.get("price").toString()));
            product.setStock(Integer.parseInt(data.get("stockQuantity").toString()));

            if (data.containsKey("categoryId") && data.get("categoryId") != null) {
                Integer catId = Integer.parseInt(data.get("categoryId").toString());
                Category cat = categoryRepository.findById(catId)
                        .orElseThrow(() -> new RuntimeException("Categoria nu a fost găsită"));
                product.setCategory(cat);
            }

            if (data.containsKey("specs") && data.get("specs") != null) {
                product.setSpecs(data.get("specs").toString());
            }

            Product saved = productRepository.save(product);

            PriceHistory history = new PriceHistory();
            history.setProduct(saved);
            history.setOldPrice(BigDecimal.ZERO);
            history.setNewPrice(saved.getPrice());
            PriceHistory savedHistory = priceHistoryRepository.save(history);

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable @NonNull Long id, @RequestBody Map<String, Object> data) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Produsul nu a fost găsit"));

            if (data.containsKey("name")) product.setName((String) data.get("name"));
            if (data.containsKey("brand")) product.setBrand((String) data.get("brand"));

            if (data.containsKey("price")) {
                BigDecimal newPrice = new BigDecimal(data.get("price").toString());
                if (product.getPrice() != null && product.getPrice().compareTo(newPrice) != 0) {
                    PriceHistory history = new PriceHistory();
                    history.setProduct(product);
                    history.setOldPrice(product.getPrice());
                    history.setNewPrice(newPrice);
                    PriceHistory savedHistory = priceHistoryRepository.save(history);
                }
                product.setPrice(newPrice);
            }

            if (data.containsKey("stockQuantity")) product.setStock(Integer.parseInt(data.get("stockQuantity").toString()));
            if (data.containsKey("categoryId")) {
                Integer catId = Integer.parseInt(data.get("categoryId").toString());
                Category cat = categoryRepository.findById(catId)
                        .orElseThrow(() -> new RuntimeException("Categoria nu a fost găsită"));
                product.setCategory(cat);
            }
            if (data.containsKey("specs")) {
                product.setSpecs(data.get("specs") != null ? data.get("specs").toString() : null);
            }

            Product saved = productRepository.save(product);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable @NonNull Long id) {
        try {
            if (!productRepository.existsById(id)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Produsul nu există"));
            }
            productRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Produs șters cu succes"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nu se poate șterge — produsul este legat de comenzi existente"));
        }
    }
}