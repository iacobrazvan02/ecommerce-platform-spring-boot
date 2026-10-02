package com.electrotech.store.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_quiz_logs")
public class AiQuizLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String quizData;

    @Column(name = "recommended_product_id")
    private Long recommendedProductId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public AiQuizLog() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getQuizData() { return quizData; }
    public void setQuizData(String quizData) { this.quizData = quizData; }
    public Long getRecommendedProductId() { return recommendedProductId; }
    public void setRecommendedProductId(Long recommendedProductId) { this.recommendedProductId = recommendedProductId; }
}