package com.bohdan.anime_library_manager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_text", nullable = false, columnDefinition = "TEXT")
    private String reviewText;

    @Column
    private Integer score;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id", nullable = false)
    private Title title;

    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    public Review(Long id, String reviewText, Integer score, LocalDateTime createdAt, Title title) {
        this.id = id;
        this.reviewText = reviewText;
        this.score = score;
        this.createdAt = createdAt;
        this.title = title;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public Integer getScore() {
        return score;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Title getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTitle(Title title) {
        this.title = title;
    }
}