package com.electrotech.store.controller;

import com.electrotech.store.model.AiQuizLog;
import com.electrotech.store.model.Product;
import com.electrotech.store.model.ProductDiscount;
import com.electrotech.store.model.User;
import com.electrotech.store.repository.AiQuizLogRepository;
import com.electrotech.store.repository.ProductDiscountRepository;
import com.electrotech.store.repository.ProductRepository;
import com.electrotech.store.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class AiQuizController {

    private final AiQuizLogRepository quizLogRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductDiscountRepository discountRepository;

    private static final Map<String, List<String>> CATEGORY_MAP = Map.of(
            "smartphone", List.of("smartphone", "telefon"),
            "laptop", List.of("laptop", "desktop"),
            "tablete", List.of("tablete", "tableta"),
            "gaming", List.of("gaming"),
            "tv", List.of("tv", "audio"),
            "accesorii", List.of("accesorii", "casti"),
            "electrocasnice_mari", List.of("electrocasnice mari"),
            "electrocasnice_mici", List.of("electrocasnice mici"));

    public AiQuizController(AiQuizLogRepository quizLogRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            ProductDiscountRepository discountRepository) {
        this.quizLogRepository = quizLogRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }


    private BigDecimal getEffectivePrice(Product product, List<ProductDiscount> activeDiscounts) {
        BigDecimal originalPrice = product.getPrice();
        if (originalPrice == null) return BigDecimal.ZERO;

        ProductDiscount discount = activeDiscounts.stream()
                .filter(d -> d.getProduct() != null && d.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (discount != null && discount.getDiscountPercent() != null && discount.getDiscountPercent() > 0) {
            BigDecimal percent = BigDecimal.valueOf(discount.getDiscountPercent());
            BigDecimal multiplier = BigDecimal.ONE.subtract(percent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            return originalPrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        }

        return originalPrice;
    }

    @PostMapping("/recommend")
    public ResponseEntity<?> submitQuiz(@RequestBody Map<String, Object> data) {
        try {
            String quizDataJson = data.get("quizData").toString();
            String usage = data.getOrDefault("usage", "general").toString().toLowerCase();
            double budget = Double.parseDouble(data.getOrDefault("budget", "5000").toString());

            List<ProductDiscount> activeDiscounts = discountRepository.findByActiveUntilAfter(LocalDateTime.now());

            List<Product> candidates = productRepository.findAll().stream()
                    .filter(p -> p.getPrice() != null && p.getStock() != null && p.getStock() > 0)
                    .filter(p -> getEffectivePrice(p, activeDiscounts).doubleValue() <= budget)
                    .toList();

            if (!usage.equals("general")) {
                List<String> keywords = CATEGORY_MAP.getOrDefault(usage, List.of(usage));

                List<Product> filtered = candidates.stream()
                        .filter(p -> {
                            String cat = p.getCategory() != null ? p.getCategory().getName().toLowerCase() : "";
                            return keywords.stream().anyMatch(cat::contains);
                        })
                        .toList();

                if (filtered.isEmpty()) {

                    List<Product> overBudget = productRepository.findAll().stream()
                            .filter(p -> p.getStock() != null && p.getStock() > 0)
                            .filter(p -> {
                                String cat = p.getCategory() != null ? p.getCategory().getName().toLowerCase() : "";
                                return keywords.stream().anyMatch(cat::contains);
                            })
                            .filter(p -> getEffectivePrice(p, activeDiscounts).doubleValue() > budget)
                            .toList();

                    if (!overBudget.isEmpty()) {
                        Product cheapest = overBudget.stream()
                                .min((a, b) -> getEffectivePrice(a, activeDiscounts)
                                        .compareTo(getEffectivePrice(b, activeDiscounts)))
                                .get();
                        BigDecimal cheapestPrice = getEffectivePrice(cheapest, activeDiscounts);
                        return ResponseEntity.ok(Map.of(
                                "message", "Nu avem produse din această categorie sub " + (int) budget +
                                        " lei. Cel mai ieftin este " + cheapest.getName() +
                                        " la " + cheapestPrice + " lei."));
                    }

                    return ResponseEntity.ok(Map.of(
                            "message", "Nu avem produse din categoria selectată momentan."));
                }

                candidates = filtered;
            }

            if (candidates.isEmpty()) {
                return ResponseEntity.ok(Map.of("message", "Nu am găsit produse în bugetul specificat."));
            }

            Product recommended = candidates.stream()
                    .max((a, b) -> getEffectivePrice(a, activeDiscounts)
                            .compareTo(getEffectivePrice(b, activeDiscounts)))
                    .orElse(candidates.get(0));

            BigDecimal effectivePrice = getEffectivePrice(recommended, activeDiscounts);

            if (data.containsKey("userId") && data.get("userId") != null) {
                try {
                    Integer userId = Integer.parseInt(data.get("userId").toString());
                    User user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        AiQuizLog log = new AiQuizLog();
                        log.setUser(user);
                        log.setQuizData(quizDataJson);
                        log.setRecommendedProductId(recommended.getId());
                        AiQuizLog savedLog = quizLogRepository.save(log);
                    }
                } catch (Exception ignored) {
                }
            }

            return ResponseEntity.ok(Map.of(
                    "productId", recommended.getId(),
                    "productName", recommended.getName(),
                    "productBrand", recommended.getBrand() != null ? recommended.getBrand() : "",
                    "productPrice", effectivePrice,
                    "message", "Bazat pe preferințele tale, îți recomandăm:"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/history/{userId}")
    public List<AiQuizLog> getQuizHistory(@PathVariable @NonNull Integer userId) {
        return quizLogRepository.findByUserId(userId);
    }
}
