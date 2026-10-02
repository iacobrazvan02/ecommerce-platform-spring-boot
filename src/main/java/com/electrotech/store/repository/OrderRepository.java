package com.electrotech.store.repository;

import com.electrotech.store.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Trebuie să adaugi importul ăsta
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);

    List<Order> findByShippingAddressId(Integer addressId);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product")
    List<Order> findAllWithItems();
}