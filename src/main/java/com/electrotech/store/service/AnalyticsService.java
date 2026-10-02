package com.electrotech.store.service;

import com.electrotech.store.model.Product;
import com.electrotech.store.model.ProductAnalytics;
import com.electrotech.store.repository.ProductAnalyticsRepository;
import com.electrotech.store.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AnalyticsService {

    private final ProductAnalyticsRepository analyticsRepository;
    private final ProductRepository productRepository;

    public AnalyticsService(ProductAnalyticsRepository analyticsRepository, ProductRepository productRepository) {
        this.analyticsRepository = analyticsRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void trackView(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();

        ProductAnalytics analytics = analyticsRepository.findByProductId(productId)
                .orElse(new ProductAnalytics());

        if (analytics.getProduct() == null) {
            analytics.setProduct(product);
            analytics.setViewCount(0);
        }

        analytics.setViewCount(analytics.getViewCount() + 1);
        analytics.setLastViewedAt(LocalDateTime.now());

        analyticsRepository.save(analytics);
    }
}