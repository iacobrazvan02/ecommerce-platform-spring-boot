package com.electrotech.store.service;

import com.electrotech.store.model.*;
import com.electrotech.store.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            ProductRepository productRepository, UserRepository userRepository,
            AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public OrderItem addToCart(Integer userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Userul nu a fost găsit"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produsul nu a fost găsit"));

        Order order = orderRepository.findByUserId(userId).stream()
                .filter(o -> "CART".equals(o.getStatus()))
                .findFirst()
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus("CART");
                    newOrder.setTotalAmount(BigDecimal.ZERO);
                    return orderRepository.save(newOrder);
                });

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPriceAtPurchase(product.getPrice());

        BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(quantity));
        order.setTotalAmount(order.getTotalAmount().add(itemTotal));
        orderRepository.save(order);

        return orderItemRepository.save(item);
    }

    public List<Order> getAllOrdersForAdmin() {
        return orderRepository.findAllWithItems();
    }

    public List<Order> getUserOrders(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public void checkout(Integer userId, Integer addressId, String paymentMethod, String deliveryMethod) {
        Address shippingAddress = null;
        if (addressId != null) {
            shippingAddress = addressRepository.findById(addressId).orElse(null);
        }

        Address finalAddress = shippingAddress;
        orderRepository.findByUserId(userId).stream()
                .filter(o -> "CART".equals(o.getStatus()))
                .forEach(o -> {
                    o.setStatus("COMPLETED");
                    if (finalAddress != null) {
                        o.setShippingAddress(finalAddress);
                    }
                    if (paymentMethod != null) {
                        o.setPaymentMethod(paymentMethod);
                    }
                    if (deliveryMethod != null) {
                        o.setDeliveryMethod(deliveryMethod);
                    }
                    orderRepository.save(o);
                });
    }

    @Transactional
    public void removeItemFromCart(Integer itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item-ul nu a fost găsit"));

        Order order = item.getOrder();

        BigDecimal itemTotal = item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity()));
        order.setTotalAmount(order.getTotalAmount().subtract(itemTotal));

        orderItemRepository.delete(item);

        if (order.getOrderItems() != null) {
            order.getOrderItems().remove(item);
            if (order.getOrderItems().isEmpty()) {
                orderRepository.delete(order);
            } else {
                orderRepository.save(order);
            }
        }
    }

    @Transactional
    public OrderItem updateItemQuantity(Integer itemId, Integer newQuantity) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item-ul nu a fost găsit"));

        Order order = item.getOrder();

        BigDecimal oldTotal = item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity()));
        order.setTotalAmount(order.getTotalAmount().subtract(oldTotal));

        item.setQuantity(newQuantity);

        BigDecimal newTotal = item.getPriceAtPurchase().multiply(new BigDecimal(newQuantity));
        order.setTotalAmount(order.getTotalAmount().add(newTotal));

        orderRepository.save(order);
        return orderItemRepository.save(item);
    }
}