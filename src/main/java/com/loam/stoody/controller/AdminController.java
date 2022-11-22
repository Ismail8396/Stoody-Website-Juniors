package com.loam.stoody.controller;

import com.loam.stoody.dto.ProductDTO;
import com.loam.stoody.model.Category;
import com.loam.stoody.model.Product;
import com.loam.stoody.model.Role;
import com.loam.stoody.repository.RoleRepository;
import com.loam.stoody.repository.UserRepository;
import com.loam.stoody.service.CategoryService;
import com.loam.stoody.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/admin")
    public String getAdminPage() {
        return "adminHome";
    }

    @GetMapping("/admin/categories")
    public String getAdminProductCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String getAdminProductCategoriesAdd(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String postAdminProductCategoriesAdd(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String getAdminProductCategoriesDelete(@PathVariable int id) {
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String getAdminProductCategoriesUpdate(@PathVariable int id, Model model) {
        Optional<Category> categoryToUpdate = categoryService.getCategoryById(id);
        if (categoryToUpdate.isPresent()) {
            model.addAttribute("category", categoryToUpdate.get());
            return "categoriesAdd";
        } else {
            return "404";
        }
    }

    // Product Section
    @GetMapping("/admin/products")
    public String getAdminProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String getAdminProductsAdd(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String postAdminProductsAdd(@ModelAttribute("productDTO") ProductDTO productDTO, @RequestParam("productImage") MultipartFile multipartFile, @RequestParam("imgName") String imgName) throws IOException {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
        product.setOwnerEmail("orxan.eliyev.orxan@gmail.com");//---Placeholder
        product.setUploadDate(LocalDateTime.now());
        product.setViewCount(0);// Start view count is always 0
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setDescription(productDTO.getDescription());
        String imageUUID;
        if (!multipartFile.isEmpty()) {
            imageUUID = multipartFile.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, multipartFile.getBytes());
        } else {
            imageUUID = imgName;
        }
        product.setImageName(imageUUID);
        productService.addProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getAdminProductsDelete(@PathVariable long id) {
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getAdminProductsUpdate(@PathVariable long id, Model model) {
        Product product = productService.getProductById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setOwnerEmail(product.getOwnerEmail());
        productDTO.setUploadDate(product.getUploadDate());
        productDTO.setViewCount(product.getViewCount());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setDescription(product.getDescription());
        product.setImageName(product.getImageName());

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("productDTO", productDTO);

        return "productsAdd";
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
