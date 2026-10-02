package com.electrotech.store.repository;

import com.electrotech.store.model.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Integer> {

    List<ProductDiscount> findByActiveUntilAfter(LocalDateTime now);

    List<ProductDiscount> findByProductId(Long productId);
}
