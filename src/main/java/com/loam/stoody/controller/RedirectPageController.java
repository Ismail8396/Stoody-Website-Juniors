package com.loam.stoody.controller;

import com.loam.stoody.global.constants.PRL;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path=PRL.redirectPageURL)
public class RedirectPageController {

    @GetMapping(params={"openCode","header","message"})
    public String getRedirectPage(@RequestParam("openCode")int openCode,
                                  @RequestParam("header")String header,
                                  @RequestParam("message")String message,
                                  Model model){
        if(openCode == PRL.openCode && header != null && message != null){
            if(!header.isBlank() && !message.isBlank()){
                model.addAttribute("header",header);
                model.addAttribute("message",message);

                return PRL.redirectPage;
            }
        }
        return "redirect:"+PRL.error404URL;
    }

    // without the open code, don't let user see this page
    @GetMapping
    public String getRedirectPage(){
        return "redirect:"+PRL.error404URL;
    }
}
