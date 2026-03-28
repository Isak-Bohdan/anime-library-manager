package com.bohdan.anime_library_manager.repository;

import com.bohdan.anime_library_manager.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}