package com.electrotech.store.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_discounts")
public class ProductDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Column(name = "active_until")
    private LocalDateTime activeUntil;

    public ProductDiscount() {}

    public Integer getId() { return id; }
    public Product getProduct() { return product; }
    public Integer getDiscountPercent() { return discountPercent; }
    public LocalDateTime getActiveUntil() { return activeUntil; }

    public void setId(Integer id) { this.id = id; }
    public void setProduct(Product product) { this.product = product; }
    public void setDiscountPercent(Integer discountPercent) { this.discountPercent = discountPercent; }
    public void setActiveUntil(LocalDateTime activeUntil) { this.activeUntil = activeUntil; }
}
