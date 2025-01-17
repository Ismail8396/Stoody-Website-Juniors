package com.loam.stoody.controller.control.moderator;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.i18n.LanguageModel;
import com.loam.stoody.model.user.User;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class LanguageController {
    private final IAuthenticationFacade authenticationFacade;
    private final CustomUserDetailsService customUserDetailsService;
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
    //--------------------------------------------------------------------------

    @GetMapping("/stoody/authorized/tables/internationalization")
    public String getLanguagesPage(Model model) {
        // TODO: Use a shortcut class for these kinds of stuffs.
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            // TODO: Check authorization
        } catch (RuntimeException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was either null or not authorized!");
            return "redirect:" + PRL.error404URL;
        }
        model.addAttribute("languageModel", new LanguageModel());
        model.addAttribute("languageModels", languageService.getAllLanguageModels());

        return "pages/dashboard/internationalization";
    }

    @PostMapping("/stoody/authorized/tables/internationalization/post")
    public String postLanguagesPage(@ModelAttribute("languageModel") LanguageModel languageModel) {
        // TODO: Use a shortcut class for these kinds of stuffs.
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            // TODO: Check authorization
        } catch (RuntimeException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was either null or not authorized!");
            return "redirect:" + PRL.error404URL;
        }

        languageService.addLanguageModel(languageModel);

        return "redirect:" + "/stoody/authorized/tables/internationalization";
    }

    @PostMapping("/stoody/authorized/tables/internationalization/delete/{id}")
    public String deleteLanguageModelById(@PathVariable("id") Integer id) {
        // TODO: Use a shortcut class for these kinds of stuffs.
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            // TODO: Check authorization
        } catch (RuntimeException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was either null or not authorized!");
            return "redirect:" + PRL.error404URL;
        }

        languageService.removeLanguageModelById(id);

        return "redirect:" + "/stoody/authorized/tables/internationalization";
    }
}
