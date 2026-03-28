package com.bohdan.anime_library_manager.service;

import com.bohdan.anime_library_manager.entity.Review;
import com.bohdan.anime_library_manager.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByTitleId(Long titleId) {
        return reviewRepository.findByTitleId(titleId);
    }

    public Optional<Review> getById(Long id) {
        return reviewRepository.findById(id);
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}