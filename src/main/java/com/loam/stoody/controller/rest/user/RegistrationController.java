package com.loam.stoody.controller.rest.user;

import com.loam.stoody.dto.api.request.RegistrationRequestDTO;
import com.loam.stoody.model.user.requests.RegistrationRequest;
import com.loam.stoody.global.constants.IndoorResponses;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.user.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = PRL.apiRegistrationPrefixURL)
public class RegistrationController {
    private final RegistrationService registrationService;
    @Autowired
    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    @PostMapping("/user/register/request")
    @ResponseBody
    public String register(@RequestBody RegistrationRequestDTO registrationRequest,
                           HttpServletRequest httpServletRequest){
        return registrationService.register(registrationRequest, httpServletRequest).toString();
    }

    @GetMapping("/user/register/verify/{token}")
    public String verifyAccount(@PathVariable("token") String token,
                                RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("response_message",registrationService.verifyAccount(token).toString());

        // We have to use redirect here with URL
        return "pages/coming-soon";
    }
}
