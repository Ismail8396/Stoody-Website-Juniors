package com.loam.stoody.controller;

import com.loam.stoody.dto.api.request.RegistrationRequestDTO;
import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.user.User;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.user.RegistrationService;
import com.loam.stoody.service.user.UserDTS;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final LanguageService languageService;
    private final UserDTS userDTS;

    @ModelAttribute("languageServiceLayer")
    public LanguageService getLanguageServiceLayer() {
        return languageService;
    }
    //------------------------------------------------------------------------------------------------------------------

    // Rest Controller
    @UnderDevelopment @Deprecated// Remove this line once it's ready
    @GetMapping(PRL.apiRegistrationPrefixURL+"/user/register/request")
    @ResponseBody
    public String register(@RequestBody RegistrationRequestDTO registrationRequest,
                           HttpServletRequest httpServletRequest){
        return registrationService.sendTokenToEmail(registrationRequest, httpServletRequest).toString();
    }

    // Kneaded Controller

    // Sign Up
    @GetMapping(PRL.signUpURL)
    public String getRegister(){
        return PRL.signUpPage;
    }

    // Register a user
    @PostMapping(PRL.signUpURL)
    public String postRegister(@ModelAttribute("user") User user,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest httpServletRequest) {
        String headerMessage;
        String messageContent;

        RegistrationRequestDTO registrationRequest = new RegistrationRequestDTO();

        // implement if user is null case
        registrationRequest.setUsername(user.getUsername());
        registrationRequest.setEmail(user.getEmail());
        registrationRequest.setPassword(user.getPassword());

        IndoorResponse response = registrationService.sendTokenToEmail(registrationRequest, httpServletRequest);

        if(response == IndoorResponse.SUCCESS){
            headerMessage = languageService.getContent("global.success");
            messageContent = "We've sent an email to you with a link. Please, click on that link within 15 minutes so your account can get approved!";
        }else if(response == IndoorResponse.USERNAME_EXIST){
            headerMessage = "Registration Failed!";
            messageContent = "It seems the username is already in use. Be sure that you have not made any registration requests recently.";
        }else if(response == IndoorResponse.EMAIL_EXIST){
            headerMessage = "Registration Failed!";
            messageContent = "It seems the email is already in use. Be sure that you have not made any registration requests recently.";
        } else if(response == IndoorResponse.USERNAME_EMAIL_EXIST){
            headerMessage = "Registration Failed!";
            messageContent = "It seems the email and username is already in use. Be sure that you have not made any registration requests recently.";
        }
        else{
            headerMessage = "Error!";
            messageContent = "I think you're lost. Definitely, this is not the page you're looking for...";
        }

        redirectAttributes.addAttribute("header", headerMessage);
        redirectAttributes.addAttribute("message", messageContent);
        redirectAttributes.addAttribute("openCode", PRL.openCode);

        return "redirect:"+PRL.redirectPageURL;
    }


    @GetMapping(PRL.signUpURL+PRL.apiVerifySuffixURL+"/{token}")
    public String verifyAccount(@PathVariable("token") String token,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest servletRequest){
        String headerMessage;
        String messageContent;

        // TODO: Replace hard-coded string literals
        IndoorResponse response = registrationService.verifyAccount(token, servletRequest);
        if(response == IndoorResponse.SUCCESS){
            headerMessage = "Success!";
            messageContent = "Your account has been verified and you're now signed in to go on as a part of our family!";
        }else if(response == IndoorResponse.TOKEN_EXPIRED){
            headerMessage = "Token Expired!";
            messageContent = "It seems that the token you used is expired. Request a new one, please.";
        }else if(response == IndoorResponse.TOKEN_ABSENT){
            headerMessage = "Invalid token!";
            messageContent = "It seems that the link you used is not correct. Please, be sure that you've not opened an old link.";
        }
        else{
            headerMessage = "Oops!...";
            messageContent = "I think you're lost. Definitely, this is not the page you're looking for...";
        }

        redirectAttributes.addAttribute("header", headerMessage);
        redirectAttributes.addAttribute("message", messageContent);
        redirectAttributes.addAttribute("openCode", PRL.openCode);

        return "redirect:"+PRL.redirectPageURL;
    }
}
