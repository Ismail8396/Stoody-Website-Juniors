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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    private final IAuthenticationFacade authenticationFacade;

    @Autowired
    public ViewController(IAuthenticationFacade authenticationFacade){
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping(value = PRL.homeURL)
    public String getHomePage(HttpServletRequest httpServletRequest){
        boolean authenticated = httpServletRequest.getSession().getAttribute("userName") != null;

//        Authentication authentication = authenticationFacade.getAuthentication();
//        boolean isAuthenticated  = authentication != null && authentication.getName() != null && !authentication.getName().equals("anonymousUser");
//
//        if(isAuthenticated)
        if(authenticated)
            return "redirect:"+PRL.userHomeURL;

        return PRL.visitorHomePage;
    }

    @GetMapping(value = PRL.userHomeURL)
    public String getUserHomePage(){
        return PRL.userHomePage;
    }
}
