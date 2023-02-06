package com.loam.stoody.controller.product;

import com.loam.stoody.dto.api.response.course.CourseCategoryResponseDTO;
import com.loam.stoody.model.product.course.CourseCategory;
import com.loam.stoody.service.product.CategoryService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class CourseCategoryController {
    private final UserDTS userDTS;
    private final CategoryService categoryService;

    @ModelAttribute("getUserDTS")
    public UserDTS getUserDTS() {
        return userDTS;
    }

    @ModelAttribute("categoryTableElements")
    public List<CourseCategory> getCategoryTableElements() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/stoody/authorized/tables/course/category")
    public String getCourseCategoryTable(Model model) {

        model.addAttribute("categoryModel", new CourseCategoryResponseDTO());
        return "pages/dashboard/admin-course-category-table";
    }

    @PostMapping("/stoody/authorized/tables/course/category/add")
    public String postCourseCategoryTable(@ModelAttribute("categoryModel") CourseCategoryResponseDTO categoryModel) {
        if(categoryModel != null) {
            if (!categoryModel.getName().isBlank()) {
                CourseCategory courseCategory = new CourseCategory();
                courseCategory.setName(categoryModel.getName());
                if(categoryModel.getParentId() != null) {
                    CourseCategory parentCategory = categoryService.getCategoryById(categoryModel.getParentId()).orElse(null);
                    if (parentCategory != null)
                        courseCategory.setParent(parentCategory);
                }
                System.out.println(categoryModel.getPublished());
                courseCategory.setPublished(categoryModel.getPublished());
                categoryService.addCategory(courseCategory);
            }
        }
        return "redirect:/stoody/authorized/tables/course/category";
    }

    @GetMapping("/stoody/authorized/tables/course/category/publish/{id}")
    public String postCourseCategoryTablePublish(@PathVariable("id") int id) {
        CourseCategory category = categoryService.getCategoryById(id).orElse(null);
        if (category != null) {
            category.setPublished(true);
            categoryService.addCategory(category);
        }
        return "redirect:/stoody/authorized/tables/course/category";
    }

    @GetMapping("/stoody/authorized/tables/course/category/draft/{id}")
    public String postCourseCategoryTableDraft(@PathVariable("id") int id) {
        CourseCategory category = categoryService.getCategoryById(id).orElse(null);
        if (category != null) {
            category.setPublished(false);
            categoryService.addCategory(category);
        }
        return "redirect:/stoody/authorized/tables/course/category";
    }

    @GetMapping("/stoody/authorized/tables/course/category/delete/{id}")
    public String postCourseCategoryTableDelete(@PathVariable("id") int id) {
        categoryService.removeCategoryById(id);
        return "redirect:/stoody/authorized/tables/course/category";
    }
}
