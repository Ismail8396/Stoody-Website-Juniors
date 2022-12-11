package com.loam.stoody.controller.user_control;

import com.loam.stoody.model.User;
import com.loam.stoody.repository.UserRepository;
import com.loam.stoody.service.CustomUserDetailService;
import com.loam.stoody.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping("/control-panel/profile")
    public String getUserProfilePage(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) auth.getPrincipal();
        model.addAttribute("userInfo",currentUser);

        System.out.println(currentUser);
        return "user_panel/profile";
    }

    @PostMapping("/control-panel/profile/save-main")
    public String postUserProfileSaveMainInfo(@ModelAttribute("userInfo") User modifiedData,
                                              RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService._findUserByUsername(auth.getName()).orElse(null);

        if(currentUser == null) {
            redirectAttributes.addFlashAttribute("error_message", "Something went wrong, your account details could not be updated!");
            return "redirect:/control-panel/profile";
        }

        // Check whether email or username is hold already by another user
        if(!modifiedData.getUsername().equals(currentUser.getUsername())){
            Optional<User> foundUser = userService._findUserByUsername(modifiedData.getUsername());
            if(foundUser.isPresent()){
                redirectAttributes.addFlashAttribute("error_message","Your account details could not be updated! Try another username.");
                return "redirect:/control-panel/profile";
            }
        }else if(!modifiedData.getEmail().equals(currentUser.getEmail())) {
            Optional<User> foundUser = userService._findUserByEmail(modifiedData.getEmail());
            if(foundUser.isPresent()){
                redirectAttributes.addFlashAttribute("error_message","Your account details could not be updated! Try another email.");
                return "redirect:/control-panel/profile";
            }
        }

        currentUser.setUsername(modifiedData.getUsername());
        currentUser.setEmail(modifiedData.getEmail());
        currentUser.setFirstName(modifiedData.getFirstName());
        currentUser.setLastName(modifiedData.getLastName());
        currentUser.setAboutUser(modifiedData.getAboutUser());

        if(userService.saveUser(currentUser)){
            redirectAttributes.addFlashAttribute("error_message","Your account details could not be updated! If you see this message, then you are encountering a technical issue which we'll fix as soon as possible.");
            return "redirect:/control-panel/profile";
        }

        redirectAttributes.addFlashAttribute("success_message","Your account details have been updated!");

        return "redirect:/control-panel/profile";
    }

    @PostMapping("/control-panel/profile/save-additionals")
    public String postUserProfileSaveAdditionalInfo(@ModelAttribute("userInfo") User modifiedData,
                                              RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService._findUserByUsername(auth.getName()).orElse(null);

        if(currentUser == null) {
            redirectAttributes.addFlashAttribute("error_message", "Your account details could not be updated! If you see this message, then you are encountering a technical issue which we'll fix as soon as possible.");
            return "redirect:/control-panel/profile";
        }

        currentUser.setSocialFacebook(modifiedData.getSocialFacebook());
        currentUser.setSocialInstagram(modifiedData.getSocialInstagram());
        currentUser.setSocialYoutube(modifiedData.getSocialYoutube());
        currentUser.setSocialGithub(modifiedData.getSocialGithub());
        currentUser.setSocialLinkedIn(modifiedData.getSocialLinkedIn());
        currentUser.setSocialTwitter(modifiedData.getSocialTwitter());
        currentUser.setCity(modifiedData.getCity());
        currentUser.setCountry(modifiedData.getCountry());

        // Update the element in DB
        if(userService.saveUser(currentUser)){
            redirectAttributes.addFlashAttribute("error_message","Your account details could not be updated! If you see this message, then you are encountering a technical issue which we'll fix as soon as possible.");
            return "redirect:/control-panel/profile";
        }

        redirectAttributes.addFlashAttribute("success_message","Your account details have been updated!");

        return "redirect:/control-panel/profile";
    }
}
