package com.electrotech.store.controller;

import com.electrotech.store.model.Product;
import com.electrotech.store.model.Review;
import com.electrotech.store.model.User;
import com.electrotech.store.repository.ProductRepository;
import com.electrotech.store.repository.ReviewRepository;
import com.electrotech.store.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewController(ReviewRepository reviewRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/average-ratings")
    public Map<Long, Double> getAverageRatings() {
        List<Review> allReviews = reviewRepository.findAll();
        Map<Long, List<Integer>> ratingsMap = new HashMap<>();

        for (Review r : allReviews) {
            Long productId = r.getProduct().getId();
            ratingsMap.computeIfAbsent(productId, k -> new java.util.ArrayList<>()).add(r.getRating());
        }

        Map<Long, Double> avgMap = new HashMap<>();
        for (Map.Entry<Long, List<Integer>> entry : ratingsMap.entrySet()) {
            double avg = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
            avgMap.put(entry.getKey(), Math.round(avg * 10.0) / 10.0);
        }
        return avgMap;
    }

    @GetMapping("/product/{productId}")
    public List<Review> getReviewsForProduct(@PathVariable @NonNull Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody Map<String, Object> data) {
        try {
            Integer userId = Integer.parseInt(data.get("userId").toString());
            Long productId = Long.parseLong(data.get("productId").toString());
            Integer rating = Integer.parseInt(data.get("rating").toString());
            String comment = data.getOrDefault("comment", "").toString();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilizator negăsit"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produs negăsit"));

            Review review = new Review();
            review.setUser(user);
            review.setProduct(product);
            review.setRating(rating);
            review.setComment(comment);

            Review saved = reviewRepository.save(review);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable @NonNull Integer id) {
        try {
            if (!reviewRepository.existsById(id)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Recenzia nu există"));
            }
            reviewRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Recenzie ștearsă"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
