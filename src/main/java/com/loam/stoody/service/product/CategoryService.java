package com.loam.stoody.service.product;

import com.loam.stoody.model.product.course.CourseCategory;
import com.loam.stoody.repository.product.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CourseCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void addCategory(CourseCategory category) {
        categoryRepository.save(category);
    }

    public void removeCategoryById(int id) {
        categoryRepository.deleteById(id);
    }

    public Optional<CourseCategory> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }
}