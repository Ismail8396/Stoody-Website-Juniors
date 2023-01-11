/*
@fileName:  ErrorControllerCustom

@aka:       Custom Error Controller

@purpose:   Created to show custom error page.

@author:    OrkhanGG

@createdAt: 18.12.2022
*/

package com.loam.stoody.controller;

import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class ErrorControllerCustom implements ErrorController {
    private final LanguageService languageService;
    private final UserDTS userDTS;

    @ModelAttribute("languageServiceLayer")
    public LanguageService getLanguageServiceLayer() {
        return languageService;
    }

    @RequestMapping(PRL.error404URL)
    public String handleError() {
        return PRL.error404Page;
    }
}