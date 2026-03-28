package com.bohdan.anime_library_manager.service;

import com.bohdan.anime_library_manager.exception.CategoryAlreadyExistsException;
import com.bohdan.anime_library_manager.entity.Category;
import com.bohdan.anime_library_manager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    private static final Set<String> DEFAULT_CATEGORY_NAMES = Set.of(
            "Planned",
            "Watching",
            "Completed",
            "Favorite",
            "Dropped"
    );

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }


    public Category saveCategory(Category category) {
        String normalizedName = category.getName().trim();

        boolean alreadyExists = categoryRepository.findAll().stream()
                .anyMatch(existing -> existing.getName().equalsIgnoreCase(normalizedName));

        if (alreadyExists) {
            throw new CategoryAlreadyExistsException(
                    "Category with name '" + normalizedName + "' already exists"
            );
        }

        category.setName(normalizedName);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (isDefaultCategory(existingCategory.getName())) {
            throw new RuntimeException("Default categories cannot be edited");
        }

        existingCategory.setName(updatedCategory.getName());
        existingCategory.setDescription(updatedCategory.getDescription());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (isDefaultCategory(category.getName())) {
            throw new RuntimeException("Default categories cannot be deleted");
        }

        categoryRepository.deleteById(id);
    }

    public boolean isDefaultCategory(String name) {
        return DEFAULT_CATEGORY_NAMES.contains(name);
    }

    public Set<String> getDefaultCategoryNames() {
        return DEFAULT_CATEGORY_NAMES;
    }
}