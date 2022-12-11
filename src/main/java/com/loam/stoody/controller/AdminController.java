package com.loam.stoody.controller;

import com.loam.stoody.dto.CourseDTO;
import com.loam.stoody.model.*;
import com.loam.stoody.repository.RoleRepository;
import com.loam.stoody.repository.UserRepository;
import com.loam.stoody.service.CategoryService;
import com.loam.stoody.service.CourseService;
import com.loam.stoody.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/courseThumbnail";
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LanguageService languageService;

    @GetMapping("/admin")
    public String getAdminPage() {
        return "adminHome";
    }

    @GetMapping("/admin/categories")
    public String getAdminCourseCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String getAdminCourseCategoriesAdd(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    @ModelAttribute("categories")
    public List<Category> listCategories(){
        return categoryService.getAllCategories();
    }

    @PostMapping("/admin/categories/add")
    public String postAdminCourseCategoriesAdd(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String getAdminCourseCategoriesDelete(@PathVariable int id) {
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String getAdminCourseCategoriesUpdate(@PathVariable int id, Model model) {
        Optional<Category> categoryToUpdate = categoryService.getCategoryById(id);
        if (categoryToUpdate.isPresent()) {
            model.addAttribute("category", categoryToUpdate.get());
            return "categoriesAdd";
        } else {
            return "404";
        }
    }

    // Course Section
    @GetMapping("/admin/courses")
    public String getAdminCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses";
    }

    @GetMapping("/admin/courses/add")
    public String getAdminCoursesAdd(Model model) {
        model.addAttribute("courseDTO", new CourseDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "coursesAdd";
    }

    @PostMapping("/admin/courses/add")
    public String postAdminCoursesAdd(@ModelAttribute("courseDTO") CourseDTO courseDTO, @RequestParam("courseThumbnail") MultipartFile multipartFile, @RequestParam("imgName") String imgName) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findUserByUsername((String)auth.getPrincipal());
        if(currentUser.isPresent()) {

            Course course = new Course();
            course.setCourseTitle(courseDTO.getCourseTitle());
            course.setCourseSubtitle(courseDTO.getCourseSubtitle());
            course.setCourseDescription(courseDTO.getCourseDescription());
            course.setThumbnailURL(courseDTO.getThumbnailURL());
            course.setPromoVideoURL(courseDTO.getPromoVideoURL());
            course.setLanguageCode(courseDTO.getLanguageCode());
            course.setLevel(courseDTO.getLevel());
            course.setWelcomeMessage(courseDTO.getWelcomeMessage());
            course.setCongratulationsMessage(courseDTO.getCongratulationsMessage());
            course.setCategory(categoryService.getCategoryById(courseDTO.getCategoryId()).get());
            course.setAuthor(currentUser.get());

            //            TODO: Set videos
//            Set<Video> videos = new HashSet<>();
//            for(int c = 0; c < courseDTO.getVideosId().size(); c++)
//                videos.add(videoRepository.);
//            course.setVideos(videos);
            course.setUploadDate(LocalDateTime.now());// Problem

            course.setCurrency(courseDTO.getCurrency());
            course.setPrice(courseDTO.getPrice());
            course.setDiscount(courseDTO.getDiscount());

//            String imageUUID;
//            if (!multipartFile.isEmpty()) {
//                imageUUID = multipartFile.getOriginalFilename();
//                Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
//                Files.write(fileNameAndPath, multipartFile.getBytes());
//            } else {
//                imageUUID = imgName;
//            }
//            course.setThumbnailURL(imageUUID);

            courseService.addCourse(course);

        }else{
            System.err.println("Could not get the current user! " + Thread.currentThread().getStackTrace()[1]);
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/admin/course/delete/{id}")
    public String getAdminCoursesDelete(@PathVariable long id) {
        courseService.removeCourseById(id);
        return "redirect:/admin/courses";
    }

    @GetMapping("/admin/course/update/{id}")
    public String getAdminCoursesUpdate(@PathVariable long id, Model model) {
        Course course = courseService.getCourseById(id).get();

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setCourseTitle(course.getCourseTitle());
        courseDTO.setCourseSubtitle(course.getCourseSubtitle());
        courseDTO.setCourseDescription(course.getCourseDescription());
        courseDTO.setThumbnailURL(course.getThumbnailURL());
        courseDTO.setPromoVideoURL(course.getPromoVideoURL());
        courseDTO.setLanguageCode(course.getLanguageCode());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setWelcomeMessage(course.getWelcomeMessage());
        courseDTO.setCongratulationsMessage(course.getCongratulationsMessage());
        courseDTO.setCategoryId(course.getCategory().getId());
        courseDTO.setAuthorId(course.getAuthor().getId());

        List<Video> videos = new ArrayList<>(course.getVideos());
        Set<Integer> videosId = new HashSet<>();
        for(int c = 0; c < videos.size(); c++)
            videosId.add(videos.get(c).getId());
        courseDTO.setVideosId(videosId);

        courseDTO.setViewCount(course.getViewCount());
        courseDTO.setUploadDate(course.getUploadDate());
        courseDTO.setCurrency(course.getCurrency());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setDiscount(course.getDiscount());

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("courseDTO", courseDTO);

        return "coursesAdd";
    }

    // -----------------------------------------
    // Users Section
    @GetMapping("/admin/users")
    public String getAdminUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/adminPageUsers";
    }

    @GetMapping("/admin/users/add")
    public String getAdminUsersAdd(Model model) {
        model.addAttribute("role", new Role());
        return "admin/adminPageUsersAdd";
    }

    @PostMapping("/admin/users/add")
    public String postAdminUsersAdd(@ModelAttribute("role") Role role) {
        roleRepository.save(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/admin/users/update/{id}")
    public String getAdminUsersUpdate(@PathVariable int id, Model model) {
        Optional<Role> roleToUpdate = roleRepository.findById(id);
        if (roleToUpdate.isPresent()) {
            model.addAttribute("role", roleToUpdate.get());
            return "admin/adminPageRolesAdd";
        } else {
            return "404";
        }
    }

    @GetMapping("/admin/users/delete/{id}")
    public String getAdminUsersDelete(@PathVariable int id) {
        roleRepository.deleteById(id);
        return "redirect:/admin/roles";
    }

    // Roles Section
    @GetMapping("/admin/roles")
    public String getAdminRoles(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/adminPageRoles";
    }

    @GetMapping("/admin/roles/add")
    public String getAdminRolesAdd(Model model) {
        model.addAttribute("role", new Role());
        return "admin/adminPageRolesAdd";
    }

    @PostMapping("/admin/roles/add")
    public String postAdminRolesAdd(@ModelAttribute("role") Role role) {
        roleRepository.save(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/admin/roles/update/{id}")
    public String getAdminRolesUpdate(@PathVariable int id, Model model) {
        Optional<Role> roleToUpdate = roleRepository.findById(id);
        if (roleToUpdate.isPresent()) {
            model.addAttribute("role", roleToUpdate.get());
            return "admin/adminPageRolesAdd";
        } else {
            return "404";
        }
    }

    @GetMapping("/admin/roles/delete/{id}")
    public String getAdminRolesDelete(@PathVariable int id) {
        roleRepository.deleteById(id);
        return "redirect:/admin/roles";
    }
}
