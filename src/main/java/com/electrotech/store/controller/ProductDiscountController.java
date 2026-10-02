package com.electrotech.store.controller;

import com.electrotech.store.model.Product;
import com.electrotech.store.model.ProductDiscount;
import com.electrotech.store.repository.ProductDiscountRepository;
import com.electrotech.store.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/discounts")
public class ProductDiscountController {

    private final ProductDiscountRepository discountRepository;
    private final ProductRepository productRepository;

    public ProductDiscountController(ProductDiscountRepository discountRepository,
                                     ProductRepository productRepository) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<ProductDiscount> getActiveDiscounts() {
        return discountRepository.findByActiveUntilAfter(LocalDateTime.now());
    }

    @GetMapping("/all")
    public List<ProductDiscount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createDiscount(@RequestBody Map<String, Object> data) {
        try {
            Long productId = Long.parseLong(data.get("productId").toString());
            Integer percent = Integer.parseInt(data.get("discountPercent").toString());
            String activeUntilStr = data.get("activeUntil").toString();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produsul nu a fost găsit"));

            ProductDiscount discount = new ProductDiscount();
            discount.setProduct(product);
            discount.setDiscountPercent(percent);
            discount.setActiveUntil(LocalDateTime.parse(activeUntilStr));

            ProductDiscount saved = discountRepository.save(discount);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/brand")
    public ResponseEntity<?> createBrandDiscount(@RequestBody Map<String, Object> data) {
        try {
            String brand = data.get("brand").toString();
            Integer percent = Integer.parseInt(data.get("discountPercent").toString());
            String activeUntilStr = data.get("activeUntil").toString();
            LocalDateTime activeUntil = LocalDateTime.parse(activeUntilStr);

            List<Product> products = productRepository.findAll().stream()
                    .filter(p -> p.getBrand() != null && p.getBrand().equalsIgnoreCase(brand))
                    .toList();

            if (products.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Nu există produse de la brandul: " + brand));
            }

            int count = 0;
            for (Product product : products) {
                ProductDiscount discount = new ProductDiscount();
                discount.setProduct(product);
                discount.setDiscountPercent(percent);
                discount.setActiveUntil(activeUntil);
                ProductDiscount saved = discountRepository.save(discount);
                count++;
            }

            return ResponseEntity.ok(Map.of("message", "Discount aplicat pe " + count + " produse de la " + brand));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable @NonNull Integer id) {
        try {
            if (!discountRepository.existsById(id)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Discountul nu există"));
            }
            discountRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Discount șters cu succes"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
