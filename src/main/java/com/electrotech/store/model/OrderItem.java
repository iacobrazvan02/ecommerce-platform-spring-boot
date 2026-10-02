package com.electrotech.store.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    @Column(name = "price_at_purchase")
    private BigDecimal priceAtPurchase;

    public OrderItem() {}

    public Integer getId() { return id; }

    public Order getOrder() { return order; }

    public Product getProduct() { return product; }

    public Integer getQuantity() { return quantity; }

    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }

    public void setId(Integer id) { this.id = id; }

    public void setOrder(Order order) { this.order = order; }

    public void setProduct(Product product) { this.product = product; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }
}