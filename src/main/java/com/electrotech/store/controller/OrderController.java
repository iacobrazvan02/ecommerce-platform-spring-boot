package com.electrotech.store.controller;

import com.electrotech.store.model.Order;
import com.electrotech.store.model.OrderItem;
import com.electrotech.store.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public OrderItem addToCart(@RequestBody Map<String, Object> data) {
        return orderService.addToCart(
                (Integer) data.get("userId"),
                Long.valueOf(data.get("productId").toString()),
                (Integer) data.get("quantity")
        );
    }

    @GetMapping("/user/{userId}")
    @Transactional
    public List<Order> getUserOrders(@PathVariable("userId") @NonNull Integer userId) {
        return orderService.getUserOrders(userId);
    }

    @GetMapping("/admin/all")
    public List<Order> getAllOrdersForAdmin() {
        return orderService.getAllOrdersForAdmin();
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<?> checkout(@PathVariable("userId") @NonNull Integer userId,
                                       @RequestBody(required = false) Map<String, Object> data) {
        try {
            Integer addressId = null;
            String paymentMethod = null;
            String deliveryMethod = null;

            if (data != null) {
                if (data.containsKey("addressId") && data.get("addressId") != null) {
                    addressId = Integer.parseInt(data.get("addressId").toString());
                }
                if (data.containsKey("paymentMethod") && data.get("paymentMethod") != null) {
                    paymentMethod = data.get("paymentMethod").toString();
                }
                if (data.containsKey("deliveryMethod") && data.get("deliveryMethod") != null) {
                    deliveryMethod = data.get("deliveryMethod").toString();
                }
            }
            orderService.checkout(userId, addressId, paymentMethod, deliveryMethod);
            return ResponseEntity.ok(Map.of("message", "Comanda a fost finalizată cu succes"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable @NonNull Integer itemId) {
        try {
            orderService.removeItemFromCart(itemId);
            return ResponseEntity.ok(java.util.Map.of("message", "Produs eliminat din coș"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/item/{itemId}/quantity")
    public ResponseEntity<?> updateItemQuantity(@PathVariable @NonNull Integer itemId,
                                                 @RequestBody Map<String, Object> data) {
        try {
            Integer quantity = (Integer) data.get("quantity");
            if (quantity == null || quantity < 1) {
                return ResponseEntity.badRequest().body(Map.of("error", "Cantitatea trebuie să fie cel puțin 1"));
            }
            OrderItem updated = orderService.updateItemQuantity(itemId, quantity);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}