package com.bohdan.anime_library_manager.service;

import com.bohdan.anime_library_manager.entity.Genre;
import com.bohdan.anime_library_manager.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getByName(String name) {
        return genreRepository.findByName(name);
    }

    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }
}