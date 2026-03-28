package com.bohdan.anime_library_manager.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "titles")
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "title_japanese", length = 255)
    private String titleJapanese;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(name = "episodes_or_chapters")
    private Integer episodesOrChapters;

    @Column(length = 100)
    private String status;

    @Column(name = "score_api", precision = 3, scale = 1)
    private BigDecimal scoreApi;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column
    private Integer year;

    @Column(name = "personal_score")
    private Integer personalScore;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "title_genres",
            joinColumns = @JoinColumn(name = "title_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public Title() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Title(Long id, Long externalId, String title, String titleJapanese, String type,
                 Integer episodesOrChapters, String status, BigDecimal scoreApi,
                 String synopsis, String imageUrl, Integer year, Integer personalScore,
                 Boolean isFavorite, LocalDateTime createdAt, LocalDateTime updatedAt,
                 Category category, List<Review> reviews, Set<Genre> genres) {
        this.id = id;
        this.externalId = externalId;
        this.title = title;
        this.titleJapanese = titleJapanese;
        this.type = type;
        this.episodesOrChapters = episodesOrChapters;
        this.status = status;
        this.scoreApi = scoreApi;
        this.synopsis = synopsis;
        this.imageUrl = imageUrl;
        this.year = year;
        this.personalScore = personalScore;
        this.isFavorite = isFavorite;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.reviews = reviews;
        this.genres = genres;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (isFavorite == null) {
            isFavorite = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleJapanese() {
        return titleJapanese;
    }

    public String getType() {
        return type;
    }

    public Integer getEpisodesOrChapters() {
        return episodesOrChapters;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getScoreApi() {
        return scoreApi;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getPersonalScore() {
        return personalScore;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Category getCategory() {
        return category;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleJapanese(String titleJapanese) {
        this.titleJapanese = titleJapanese;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEpisodesOrChapters(Integer episodesOrChapters) {
        this.episodesOrChapters = episodesOrChapters;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setScoreApi(BigDecimal scoreApi) {
        this.scoreApi = scoreApi;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setPersonalScore(Integer personalScore) {
        this.personalScore = personalScore;
    }

    public void setIsFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setTitle(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setTitle(null);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
        genre.getTitles().add(this);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getTitles().remove(this);
    }
}