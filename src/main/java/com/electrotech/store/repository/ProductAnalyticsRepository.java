package com.electrotech.store.repository;

import com.electrotech.store.model.ProductAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductAnalyticsRepository extends JpaRepository<ProductAnalytics, Long> {
    Optional<ProductAnalytics> findByProductId(Long productId);
}