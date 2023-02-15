package com.loam.stoody.service.product;

import com.loam.stoody.model.product.course.CourseCategory;
import com.loam.stoody.repository.product.CourseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CourseCategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CourseCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CourseCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<CourseCategory> getPublishedCategories() {
        return categoryRepository.findAll().stream().filter(CourseCategory::getPublished).toList();
    }

    public List<CourseCategory> getDraftCategories() {
        return categoryRepository.findAll().stream().filter(e->!e.getPublished()).toList();
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