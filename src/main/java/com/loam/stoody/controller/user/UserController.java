package com.loam.stoody.controller.user;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.global.constants.MiscConstants;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.user.User;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
class UserController {
    private final CustomUserDetailsService customUserDetailsService;
    private final IAuthenticationFacade authenticationFacade;
    private final LanguageService languageService;
    private final UserDTS userDTS;

    @ModelAttribute("getUserDTS")
    public UserDTS getUserDTS() {
        return userDTS;
    }

    @ModelAttribute("languageServiceLayer")
    public LanguageService getLanguageServiceLayer() {
        return languageService;
    }

    @GetMapping("/users/{username}")
    public String getUserProfileDisplayPage(@PathVariable("username") String username, Model model) {
        User userToPreview = null;
        try {
            customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            userToPreview = customUserDetailsService.getUserByUsername(username);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        model.addAttribute("mainPreviewUserInfo", userToPreview);
        return "pages/instructor-profile";
    }
    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/user/dashboard/standard/profile/edit")
    public String getUserProfileEditPage(Model model) {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        model.addAttribute("userObject", user);
        model.addAttribute("countries", MiscConstants.countries);
        model.addAttribute("states", MiscConstants.states);
        return "pages/profile-edit";
    }

    @PostMapping("/user/dashboard/standard/profile/edit")
    public String postUserProfileEditPage(@ModelAttribute("userObject") User userDTO) {
        if (userDTO == null) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }

        user.getUserProfile().setFirstName(userDTO.getUserProfile().getFirstName());
        user.getUserProfile().setLastName(userDTO.getUserProfile().getLastName());
        user.getUserProfile().setDateOfBirth(userDTO.getUserProfile().getDateOfBirth());
        user.getUserProfile().setAddressLine(userDTO.getUserProfile().getAddressLine());
        user.getUserProfile().setAddressLineAddition(userDTO.getUserProfile().getAddressLineAddition());
        user.getUserProfile().setState(userDTO.getUserProfile().getState());
        user.getUserProfile().setCountry(userDTO.getUserProfile().getCountry());

        customUserDetailsService.saveUser(user);

        return "redirect:" + "/user/dashboard/standard/profile/edit";
    }

    @GetMapping("/user/dashboard/standard/profile/security")
    public String getUserSecuritySettingsPage() {
        try {
            customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/security";
    }

    @GetMapping("/user/dashboard/standard/profile/social")
    public String getUserSocialProfilesSettingsPage() {
        try {
            customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/social-profile";
    }

    @GetMapping("/user/dashboard/standard/profile/notifications")
    public String getUserNotificationSettings() {
        try {
            customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }

        return "pages/notifications";
    }

    @GetMapping("/user/dashboard/standard/profile/privacy")
    public String getUserSettings(Model model) {
        try {
            customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/profile-privacy";
    }

    @GetMapping("/user/dashboard/standard/profile/delete")
    public String getUserDeleteProfile(Model model) {
        try {
            customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/delete-profile";
    }

    @GetMapping("/user/dashboard/standard/profile/accounts")
    public String getUserLinkedAccounts() {
        try {
            customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/linked-accounts";
    }
}
