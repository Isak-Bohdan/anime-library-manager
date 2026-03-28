package com.bohdan.anime_library_manager.repository;

import com.bohdan.anime_library_manager.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title, Long> {

    Optional<Title> findByExternalId(Long externalId);

    List<Title> findByType(String type);

    List<Title> findByCategoryId(Long categoryId);

    List<Title> findByIsFavoriteTrue();
}