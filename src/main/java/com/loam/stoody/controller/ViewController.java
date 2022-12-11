package com.loam.stoody.controller;

import com.loam.stoody.service.CategoryService;
import com.loam.stoody.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class ViewController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CourseService courseService;

    //@GetMapping({"","/","/home"})
    public String getLandingPage(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("courses", courseService.getAllCourses());
        return "public/index";
    }

    @GetMapping({"","/","/home"})
    public String welcome(Map<String, Object> model) {
        return "testIndex";
    }

    @GetMapping("/shop")
    public String getShopPage(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("courses", courseService.getAllCourses());
        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String getShopPage(Model model, @PathVariable int id){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("courses", courseService.getAllCoursesByCategoryId(id));
        return "shop";
    }

    @GetMapping("/shop/viewcourse/{id}")
    public String getViewCourseById(Model model, @PathVariable int id){
        model.addAttribute("course", courseService.getCourseById(id).get());
        return "viewCourse";
    }
}
