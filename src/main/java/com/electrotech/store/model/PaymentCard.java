package com.electrotech.store.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "payment_cards")
public class PaymentCard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "card_holder")
    private String cardHolder;

    @Column(name = "card_number_last4")
    private String cardNumberLast4;

    @Column(name = "card_expiry")
    private String cardExpiry;

    @Column(name = "card_brand")
    private String cardBrand; // VISA, MASTERCARD, etc.

    @Column(name = "is_default")
    private Boolean isDefault = false;

    public PaymentCard() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getCardHolder() { return cardHolder; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }
    public String getCardNumberLast4() { return cardNumberLast4; }
    public void setCardNumberLast4(String cardNumberLast4) { this.cardNumberLast4 = cardNumberLast4; }
    public String getCardExpiry() { return cardExpiry; }
    public void setCardExpiry(String cardExpiry) { this.cardExpiry = cardExpiry; }
    public String getCardBrand() { return cardBrand; }
    public void setCardBrand(String cardBrand) { this.cardBrand = cardBrand; }
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
}
