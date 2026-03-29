package com.bohdan.anime_library_manager.controller.web;

import com.bohdan.anime_library_manager.dto.JikanAnimeDto;
import com.bohdan.anime_library_manager.entity.Category;
import com.bohdan.anime_library_manager.entity.Title;
import com.bohdan.anime_library_manager.service.CategoryService;
import com.bohdan.anime_library_manager.service.FileStorageService;
import com.bohdan.anime_library_manager.service.JikanService;
import com.bohdan.anime_library_manager.service.TitleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Controller
public class TitleController {

    private final TitleService titleService;
    private final CategoryService categoryService;
    private final JikanService jikanService;
    private final FileStorageService fileStorageService;

    public TitleController(TitleService titleService,
                           CategoryService categoryService,
                           JikanService jikanService,
                           FileStorageService fileStorageService) {
        this.titleService = titleService;
        this.categoryService = categoryService;
        this.jikanService = jikanService;
        this.fileStorageService = fileStorageService;
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
    public String createTitle(@Valid @ModelAttribute("titleForm") Title title,
                              BindingResult bindingResult,
                              @RequestParam(value = "categoryId", required = false) Long categoryId,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("pageTitle", "Додати тайтл");
            return "title-form";
        }

        applyCategory(title, categoryId);

        String uploadedImagePath = fileStorageService.saveFile(imageFile);
        if (uploadedImagePath != null) {
            title.setImageUrl(uploadedImagePath);
        }

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
                              @Valid @ModelAttribute("titleForm") Title updatedTitle,
                              BindingResult bindingResult,
                              @RequestParam(value = "categoryId", required = false) Long categoryId,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              Model model) {

        Title existingTitle = titleService.getTitleById(id)
                .orElseThrow(() -> new RuntimeException("Title not found with id: " + id));

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("pageTitle", "Редагувати тайтл");
            return "title-form";
        }

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

        String uploadedImagePath = fileStorageService.saveFile(imageFile);
        if (uploadedImagePath != null) {
            existingTitle.setImageUrl(uploadedImagePath);
        }

        titleService.saveTitle(existingTitle);
        return "redirect:/titles";
    }

    @PostMapping("/titles/{id}/delete")
    public String deleteTitle(@PathVariable Long id) {
        titleService.deleteTitle(id);
        return "redirect:/titles";
    }

    @PostMapping("/titles/import/{malId}")
    public String importFromJikan(@PathVariable Long malId,
                                  @RequestParam(defaultValue = "anime") String type) {

        if (titleService.getByExternalId(malId).isPresent()) {
            return "redirect:/titles";
        }

        JikanAnimeDto item;

        if (type.equals("manga")) {
            item = jikanService.getMangaById(malId);
        } else {
            item = jikanService.getAnimeById(malId);
        }

        if (item == null) {
            return "redirect:/search";
        }

        Title title = new Title();
        title.setExternalId(item.getMalId());
        title.setTitle(item.getTitle());
        title.setType(type);
        title.setEpisodesOrChapters(item.getEpisodes());
        title.setStatus(item.getStatus());
        title.setYear(item.getYear());
        title.setSynopsis(item.getSynopsis());
        title.setImageUrl(item.getImageUrl());

        if (item.getScore() != null) {
            title.setScoreApi(BigDecimal.valueOf(item.getScore()));
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