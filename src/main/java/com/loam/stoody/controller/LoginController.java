package com.loam.stoody.controller;

import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.communication.sms.SmsSenderService;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.user.LoginServiceProxy;
import com.loam.stoody.service.user.UserDTS;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;


@Controller
@AllArgsConstructor
public class LoginController {
    private final LoginServiceProxy loginServiceProxy;
    private final LanguageService languageService;

    @ModelAttribute("languageServiceLayer")
    public LanguageService getLanguageServiceLayer() {
        return languageService;
    }
    //------------------------------------------------------------------------------------------------------------------

    @GetMapping(PRL.signInURL)
    public String getSignIn(){
        return PRL.signInPage;
    }

    @GetMapping("/logout")
    public String getLogOutTest(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:"+PRL.signInURL;
    }

    @SneakyThrows
    @ResponseBody
    @PostMapping(PRL.apiLoginPrefixURL+PRL.signInURL)
    public OutdoorResponse<?> login(@RequestParam(name = "username") String username,
                                 @RequestParam(name = "password") String password, HttpServletRequest request) {
        return loginServiceProxy.login(username, password, request);
    }

    @PostMapping(PRL.apiLoginPrefixURL+PRL.signInURL+PRL.apiVerifySuffixURL)
    @ResponseBody
    public  OutdoorResponse<?> validateOTP(@RequestParam(name = "token") String token,
                                           @RequestParam(name = "username") String username) {
        return loginServiceProxy.verifyOTP(token, username);
    }

}
