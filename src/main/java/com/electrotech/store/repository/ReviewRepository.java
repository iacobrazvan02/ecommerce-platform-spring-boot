package com.electrotech.store.repository;

import com.electrotech.store.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductId(Long productId);
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);
}
