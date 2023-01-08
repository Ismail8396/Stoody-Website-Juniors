/*
@fileName:  ViewController

@aka:       View Controller

@purpose:   Handles HTTP requests, and it returns with valid HTML response. (Intended for unauthenticated requests / visitors)

@author:    OrkhanGG

@createdAt: 20.12.2022
*/

package com.loam.stoody.controller;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.user.User;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ViewController {
    private final IAuthenticationFacade authenticationFacade;
    private final CustomUserDetailsService customUserDetailsService;
    private final LanguageService languageService;

    @Autowired
    public ViewController(IAuthenticationFacade authenticationFacade,
                          CustomUserDetailsService customUserDetailsService,
                          LanguageService languageService) {
        this.authenticationFacade = authenticationFacade;
        this.customUserDetailsService = customUserDetailsService;
        this.languageService = languageService;
    }

    @GetMapping(value = PRL.homeURL)
    public String getHomePage(Model model) {

        Authentication authentication = authenticationFacade.getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.getName() != null && !authentication.getName().equals("anonymousUser");

        if (isAuthenticated)
            return "redirect:" + PRL.userHomeURL;

        return PRL.visitorHomePage;
    }

    @GetMapping(value = PRL.userHomeURL)
    public String getUserHomePage(Model model) {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        model.addAttribute("userInfo", user);

        return PRL.userHomePage;
    }

    // Required for all controllers!
    @ModelAttribute("languageServiceLayer")
    public LanguageService getLanguageServiceLayer() {
        return languageService;
    }
}
