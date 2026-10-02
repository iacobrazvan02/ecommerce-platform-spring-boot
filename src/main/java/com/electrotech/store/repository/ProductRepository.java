package com.electrotech.store.repository;

import com.electrotech.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.* FROM products p " +
            "JOIN product_analytics pa ON p.id = pa.product_id " +
            "ORDER BY pa.view_count DESC LIMIT 3", nativeQuery = true)
    List<Product> findTop3MostViewed();

    List<Product> findByNameContainingIgnoreCase(String name);
}