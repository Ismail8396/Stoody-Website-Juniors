/*
@fileName:  LoginController

@aka:       Login Controller

@purpose:   Handles HTTP requests, and it returns with HTML response.

@author:    OrkhanGG

@createdAt: 01.12.2022
*/

package com.loam.stoody.controller.standard;

import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.user.Role;
import com.loam.stoody.model.user.User;
import com.loam.stoody.repository.user.RoleRepository;
import com.loam.stoody.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserViewController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserViewController(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                              RoleRepository roleRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Forget Password
    @GetMapping(PRL.forgetPasswordURL)
    public String getForgetPassword(){
        return PRL.forgetPasswordPage;
    }

    // Sign In
    @GetMapping(PRL.signInURL)
    public String getLogin(){
        return PRL.signInPage;
    }

    // Sign Up
    @GetMapping(PRL.signUpURL)
    public String getRegister(){
        return PRL.signUpPage;
    }

    // Register a user
    @PostMapping(PRL.signUpURL)
    public String registerPost(@ModelAttribute("user") User user,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) throws ServletException {

        // An existing user with username or email----------------------------
        // TODO: Implement i18n for the hard-coded string literals!
        String errorMessage = null;
        if(userRepository.findUserByUsername(user.getUsername()).isPresent()){
            errorMessage = "This username is already in use!";
        } else if(userRepository.findUserByEmail(user.getEmail()).isPresent()){
            errorMessage= "This email is already in use!";
        }
        if(errorMessage != null) {
            redirectAttributes.addFlashAttribute("error_message", "This email is already in use!");
            return "redirect:"+PRL.signUpURL+"?error=true";
        }
        //--------------------------------------------------------------------

        // Encode and set the password for the user
        String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));

        // Set ROLE_USER for the user
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(2).get());// Simple User Role
        user.setRoles(roles);

        // Debug Logger
        StoodyLogger.DebugLog(ConsoleColors.YELLOW, "User registered: "+ user);

        // Save user
        userRepository.save(user);

        // Login the registered user right away
        request.login(user.getUsername(), password);

        // redirect to main page
        return "redirect:"+PRL.homeURL;
    }
}
