package com.loam.stoody.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerCustom implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        //do something like logging
        return "pages/404-error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}