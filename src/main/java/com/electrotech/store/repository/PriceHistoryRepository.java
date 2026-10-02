package com.electrotech.store.repository;

import com.electrotech.store.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Integer> {
    List<PriceHistory> findByProductIdOrderByChangeDateDesc(Long productId);
}
