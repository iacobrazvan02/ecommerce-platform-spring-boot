package com.electrotech.store.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_analytics")
public class ProductAnalytics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "last_viewed_at")
    private LocalDateTime lastViewedAt;

    public ProductAnalytics() {}
    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public LocalDateTime getLastViewedAt() { return lastViewedAt; }
    public void setLastViewedAt(LocalDateTime lastViewedAt) { this.lastViewedAt = lastViewedAt; }
}