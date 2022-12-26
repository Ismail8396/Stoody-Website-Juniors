package com.loam.stoody.controller.rest.user;

import com.loam.stoody.global.constants.IndoorResponses;
import com.loam.stoody.service.communication.sms.SmsSenderService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.loam.stoody.global.constants.PRL.apiLoginPrefixURL;

@RestController
@RequestMapping(path = apiLoginPrefixURL)
@AllArgsConstructor
public class LoginController {

    private final CustomUserDetailsService customUserDetailsService;
    private final SmsSenderService smsSenderService;

    @PostMapping("/login")
    public IndoorResponses login(@RequestParam(name = "username") String username,
                                 @RequestParam(name = "password") String password) {
        customUserDetailsService.loadUserByUsername(username, password);
        return IndoorResponses.SUCCESS;
    }

    @GetMapping("/validate-otp")
    public boolean validateOTP(@RequestParam(name = "token") String token, @RequestParam(name = "username") String username) {
        return smsSenderService.validateOTP(token, username);
    }

}
