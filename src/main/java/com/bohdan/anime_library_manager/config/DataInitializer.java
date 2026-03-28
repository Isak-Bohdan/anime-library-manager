package com.bohdan.anime_library_manager.config;

import com.bohdan.anime_library_manager.entity.Category;
import com.bohdan.anime_library_manager.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryService categoryService;

    public DataInitializer(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) {
        createCategoryIfNotExists("Planned", "Заплановано до перегляду або читання");
        createCategoryIfNotExists("Watching", "Користувач зараз дивиться або читає");
        createCategoryIfNotExists("Completed", "Повністю завершено");
        createCategoryIfNotExists("Favorite", "Улюблені тайтли");
        createCategoryIfNotExists("Dropped", "Покинуто");
    }

    private void createCategoryIfNotExists(String name, String description) {
        if (categoryService.getCategoryByName(name).isEmpty()) {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            categoryService.saveCategory(category);
        }
    }
}