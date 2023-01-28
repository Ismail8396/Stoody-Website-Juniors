package com.loam.stoody.controller;

import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class VerifyPasswordController {
    private final LanguageService languageService;

    @ModelAttribute("languageServiceLayer")
    public LanguageService getLanguageServiceLayer() {
        return languageService;
    }
    //------------------------------------------------------------------------------------------------------------------

    @GetMapping(path=PRL.verifyPasswordPageURL,params={"openCode"})
    public String getVerifyPasswordPage(@RequestParam("openCode")int openCode){
        if(openCode == PRL.openCode){
            return PRL.verifyPasswordPage;
        }
        return "redirect:"+PRL.error404URL;
    }

    // without open code, return 404
    @GetMapping(PRL.verifyPasswordPageURL)
    public String getVerifyPasswordPage(){
        return "redirect:"+PRL.error404URL;
    }
}
