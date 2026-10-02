package com.electrotech.store.controller;
import com.electrotech.store.model.Category;
import com.electrotech.store.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @GetMapping
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}