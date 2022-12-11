package com.loam.stoody.service;

import com.loam.stoody.model.Category;
import com.loam.stoody.repository.CategoryRepository;
import com.loam.stoody.repository.CourseRepository;
import com.loam.stoody.repository.UserRepository;
import com.loam.stoody.repository.VideoRepository;
import com.loam.stoody.service.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    public void removeCategoryById(int id){
        categoryRepository.deleteById(id);
    }

    public Optional<Category> getCategoryById(int id){
        return categoryRepository.findById(id);
    }
}
