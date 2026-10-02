package com.electrotech.store.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @JsonManagedReference

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @Column(name = "created_at")
    private LocalDateTime orderDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "delivery_method")
    private String deliveryMethod;

    public Order() {}

    public Integer getId() { return id; }
    public User getUser() { return user; }
    public String getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public Address getShippingAddress() { return shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getDeliveryMethod() { return deliveryMethod; }

    public void setId(Integer id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setDeliveryMethod(String deliveryMethod) { this.deliveryMethod = deliveryMethod; }
}