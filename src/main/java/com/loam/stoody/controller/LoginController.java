package com.loam.stoody.controller;

import com.loam.stoody.service.communication.sms.SmsSenderService;
import com.loam.stoody.service.user.LoginServiceProxy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.loam.stoody.global.constants.PRL.apiLoginPrefixURL;

@RestController
@RequestMapping(path = apiLoginPrefixURL)
@AllArgsConstructor
public class LoginController {

    private final LoginServiceProxy loginServiceProxy;
    private final SmsSenderService smsSenderService;

    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam(name = "username") String username,
                                   @RequestParam(name = "password") String password, HttpServletRequest request) {
        return loginServiceProxy.login(username, password, request);
    }

    @GetMapping("/validate-otp")
    public  ResponseEntity<?> validateOTP(@RequestParam(name = "token") String token, @RequestParam(name = "username") String username) {
        return loginServiceProxy.verifyOTP(token, username);
    }

}
