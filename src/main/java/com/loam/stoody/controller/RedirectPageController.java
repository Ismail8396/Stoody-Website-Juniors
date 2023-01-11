package com.loam.stoody.controller;

import com.loam.stoody.dto.view.HTMLButton;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path=PRL.redirectPageURL)
public class RedirectPageController {
    private final LanguageService languageService;
    private final UserDTS userDTS;

    // TODO: Remove if it is not necessary
    @ModelAttribute("getUserDTS")
    public UserDTS getUserDTS(){
        return userDTS;
    }

    @ModelAttribute("languageServiceLayer")
    public LanguageService getLanguageServiceLayer() {
        return languageService;
    }
    //------------------------------------------------------------------------------------------------------------------

    @GetMapping(params={"openCode","header","message"})
    public String getRedirectPage(@RequestParam("openCode")int openCode,
                                  @RequestParam("header")String header,
                                  @RequestParam("message")String message,
                                  Model model){
        if(openCode == PRL.openCode && header != null && message != null){
            if(!header.isBlank() && !message.isBlank()){
                model.addAttribute("header",header);
                model.addAttribute("message",message);

                model.addAttribute("buttons",new ArrayList<>(List.of(
                        new HTMLButton("/sign-in","Sign In"),
                        new HTMLButton("/contact","Contact"),
                        new HTMLButton("/","Back to Safety")
                )));

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
