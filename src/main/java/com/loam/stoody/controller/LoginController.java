package com.loam.stoody.controller;

import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.communication.sms.SmsSenderService;
import com.loam.stoody.service.user.LoginServiceProxy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
public class LoginController {

    private final LoginServiceProxy loginServiceProxy;
    private final SmsSenderService smsSenderService;

    @GetMapping(PRL.signInURL)
    public String getSignIn(){
        return PRL.signInPage;
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
                                           @RequestParam(name = "username") String username
                                           ,HttpServletRequest request) {
        return loginServiceProxy.verifyOTP(token, username);
    }

}
