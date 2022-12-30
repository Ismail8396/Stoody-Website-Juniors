package com.loam.stoody.controller;

import com.loam.stoody.global.constants.PRL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerifyPasswordController {
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
