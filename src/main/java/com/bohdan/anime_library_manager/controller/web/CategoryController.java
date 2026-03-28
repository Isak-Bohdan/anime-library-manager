package com.bohdan.anime_library_manager.controller.web;

import com.bohdan.anime_library_manager.entity.Category;
import com.bohdan.anime_library_manager.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("defaultCategoryNames", categoryService.getDefaultCategoryNames());
        return "categories";
    }

    @GetMapping("/categories/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Створення категорії");
        return "category-form";
    }

    @PostMapping("/categories")
    public String createCategory(Category category, Model model) {
        try {
            categoryService.saveCategory(category);
            return "redirect:/categories";
        } catch (RuntimeException ex) {
            model.addAttribute("category", category);
            model.addAttribute("pageTitle", "Створення категорії");
            model.addAttribute("errorMessage", ex.getMessage());
            return "category-form";
        }
    }

    @GetMapping("/categories/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (categoryService.isDefaultCategory(category.getName())) {
            throw new RuntimeException("Default categories cannot be edited");
        }

        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "Редагування категорії");
        return "category-form";
    }

    @PostMapping("/categories/{id}")
    public String updateCategory(@PathVariable Long id, Category updatedCategory) {
        categoryService.updateCategory(id, updatedCategory);
        return "redirect:/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}