package com.electrotech.store.repository;

import com.electrotech.store.model.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Integer> {
    List<PaymentCard> findByUserId(Integer userId);
}
