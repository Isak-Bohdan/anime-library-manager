package com.bohdan.anime_library_manager.controller.web;

import com.bohdan.anime_library_manager.dto.JikanAnimeDto;
import com.bohdan.anime_library_manager.entity.Category;
import com.bohdan.anime_library_manager.entity.Title;
import com.bohdan.anime_library_manager.service.CategoryService;
import com.bohdan.anime_library_manager.service.JikanService;
import com.bohdan.anime_library_manager.service.TitleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
public class TitleController {

    private final TitleService titleService;
    private final CategoryService categoryService;
    private final JikanService jikanService;

    public TitleController(TitleService titleService,
                           CategoryService categoryService,
                           JikanService jikanService) {
        this.titleService = titleService;
        this.categoryService = categoryService;
        this.jikanService = jikanService;
    }

    @GetMapping("/titles")
    public String getAllTitles(Model model) {
        model.addAttribute("titles", titleService.getAllTitles());
        return "titles";
    }

    @GetMapping("/titles/new")
    public String showCreateForm(Model model) {
        model.addAttribute("titleForm", new Title());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("pageTitle", "Додати тайтл");
        return "title-form";
    }

    @PostMapping("/titles")
    public String createTitle(@ModelAttribute("titleForm") Title title,
                              @RequestParam(value = "categoryId", required = false) Long categoryId) {

        applyCategory(title, categoryId);
        titleService.saveTitle(title);
        return "redirect:/titles";
    }

    @GetMapping("/titles/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Title title = titleService.getTitleById(id)
                .orElseThrow(() -> new RuntimeException("Title not found with id: " + id));

        model.addAttribute("titleForm", title);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("pageTitle", "Редагувати тайтл");
        return "title-form";
    }
    @PostMapping("/titles/{id}")
    public String updateTitle(@PathVariable Long id,
                              @ModelAttribute("titleForm") Title updatedTitle,
                              @RequestParam(value = "categoryId", required = false) Long categoryId) {

        Title existingTitle = titleService.getTitleById(id)
                .orElseThrow(() -> new RuntimeException("Title not found with id: " + id));

        existingTitle.setExternalId(updatedTitle.getExternalId());
        existingTitle.setTitle(updatedTitle.getTitle());
        existingTitle.setTitleJapanese(updatedTitle.getTitleJapanese());
        existingTitle.setType(updatedTitle.getType());
        existingTitle.setEpisodesOrChapters(updatedTitle.getEpisodesOrChapters());
        existingTitle.setStatus(updatedTitle.getStatus());
        existingTitle.setScoreApi(updatedTitle.getScoreApi());
        existingTitle.setSynopsis(updatedTitle.getSynopsis());
        existingTitle.setImageUrl(updatedTitle.getImageUrl());
        existingTitle.setYear(updatedTitle.getYear());
        existingTitle.setPersonalScore(updatedTitle.getPersonalScore());
        existingTitle.setIsFavorite(updatedTitle.getIsFavorite());

        applyCategory(existingTitle, categoryId);

        titleService.saveTitle(existingTitle);
        return "redirect:/titles";
    }

    @PostMapping("/titles/{id}/delete")
    public String deleteTitle(@PathVariable Long id) {
        titleService.deleteTitle(id);
        return "redirect:/titles";
    }


    @PostMapping("/titles/import/{malId}")
    public String importFromJikan(@PathVariable Long malId) {
        if (titleService.getByExternalId(malId).isPresent()) {
            return "redirect:/titles";
        }

        JikanAnimeDto anime = jikanService.getAnimeById(malId);

        if (anime == null) {
            return "redirect:/search";
        }

        Title title = new Title();
        title.setExternalId(anime.getMalId());
        title.setTitle(anime.getTitle());
        title.setType("anime");
        title.setEpisodesOrChapters(anime.getEpisodes());
        title.setStatus(anime.getStatus());
        title.setYear(anime.getYear());
        title.setSynopsis(anime.getSynopsis());

        if (anime.getScore() != null) {
            title.setScoreApi(BigDecimal.valueOf(anime.getScore()));
        }

        Category defaultCategory = categoryService.getCategoryByName("Planned").orElse(null);
        title.setCategory(defaultCategory);
        title.setIsFavorite(false);

        titleService.saveTitle(title);

        return "redirect:/titles";
    }

    private void applyCategory(Title title, Long categoryId) {
        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId).orElse(null);
            title.setCategory(category);
        } else {
            title.setCategory(null);
        }
    }
}