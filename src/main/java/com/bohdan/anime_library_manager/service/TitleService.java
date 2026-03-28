package com.bohdan.anime_library_manager.service;

import com.bohdan.anime_library_manager.entity.Title;
import com.bohdan.anime_library_manager.repository.TitleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitleService {

    private final TitleRepository titleRepository;

    public TitleService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

    public Optional<Title> getTitleById(Long id) {
        return titleRepository.findById(id);
    }

    public Optional<Title> getByExternalId(Long externalId) {
        return titleRepository.findByExternalId(externalId);
    }

    public List<Title> getByType(String type) {
        return titleRepository.findByType(type);
    }

    public List<Title> getFavorites() {
        return titleRepository.findByIsFavoriteTrue();
    }

    public Title saveTitle(Title title) {
        return titleRepository.save(title);
    }

    public void deleteTitle(Long id) {
        titleRepository.deleteById(id);
    }
}