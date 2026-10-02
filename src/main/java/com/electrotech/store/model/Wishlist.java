package com.electrotech.store.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist")
public class Wishlist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Wishlist() {}
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}