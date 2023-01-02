package com.loam.stoody.controller;

import com.loam.stoody.model.user.User;
import com.loam.stoody.service.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @GetMapping("/profile/edit")
    public String getUserProfileEditPage(Model model){

        // TODO: Get current user- needed
        User test = customUserDetailsService.getDefaultUser();
        test.setUsername("Helloresko");
        test.setEmail("orxan@gmail.com");
        test.setPassword("Orxan11");

        model.addAttribute("userInfo",test);

        return "pages/profile-edit";
    }

    @GetMapping("/student/subscriptions")
    public String getUserSubscriptions(){
        return "pages/student-subscriptions";
    }

    @GetMapping("/user/security")
    public String getUserSettings(){
        return "pages/security";
    }
}
