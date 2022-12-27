package com.loam.stoody.controller;

import com.loam.stoody.global.constants.PRL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path=PRL.redirectPageURL)
public class RedirectPageController {
    @GetMapping
    public String getRedirectPage(){
        return PRL.redirectPage;
    }
}
