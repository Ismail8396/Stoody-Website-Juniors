/*
@fileName:  ErrorControllerCustom

@aka:       Custom Error Controller

@purpose:   Created to show custom error page.

@author:    OrkhanGG

@createdAt: 18.12.2022
*/

package com.loam.stoody.controller.controllers;

import com.loam.stoody.global.constants.PRL;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerCustom implements ErrorController {

    @RequestMapping(PRL.error404URL)
    public String handleError() {
        return PRL.error404Page;
    }
}