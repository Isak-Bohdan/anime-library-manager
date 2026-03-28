package com.bohdan.anime_library_manager.repository;

import com.bohdan.anime_library_manager.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTitleId(Long titleId);
}