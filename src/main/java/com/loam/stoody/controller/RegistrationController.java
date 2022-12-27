package com.loam.stoody.controller;

import com.loam.stoody.dto.api.request.RegistrationRequestDTO;
import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.requests.RegistrationRequest;
import com.loam.stoody.global.constants.IndoorResponses;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.user.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {
    private final RegistrationService registrationService;
    @Autowired
    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

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

        IndoorResponses response = registrationService.sendTokenToEmail(registrationRequest, httpServletRequest);

        if(response == IndoorResponses.SUCCESS){
            headerMessage = "Success!";
            messageContent = "We've sent an email to you with a link. Please, click on that link within 15 minutes so your account can get approved!";
        }else if(response == IndoorResponses.USERNAME_EXIST){
            headerMessage = "Registration Failed!";
            messageContent = "It seems the username is already in use. Be sure that you have not made any registration requests recently.";
        }else if(response == IndoorResponses.EMAIL_EXIST){
            headerMessage = "Registration Failed!";
            messageContent = "It seems the email is already in use. Be sure that you have not made any registration requests recently.";
        } else if(response == IndoorResponses.USERNAME_EMAIL_EXIST){
            headerMessage = "Registration Failed!";
            messageContent = "It seems the email and username is already in use. Be sure that you have not made any registration requests recently.";
        }
        else{
            headerMessage = "Error!";
            messageContent = "I think you're lost. Definitely, this is not the page you're looking for...";
        }

        redirectAttributes.addFlashAttribute("header", headerMessage);
        redirectAttributes.addFlashAttribute("message", messageContent);

        return "redirect:"+PRL.redirectPageURL;
    }


    @GetMapping(PRL.signUpURL+PRL.apiVerifySuffixURL+"/{token}")
    public String verifyAccount(@PathVariable("token") String token,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest servletRequest){
        String headerMessage;
        String messageContent;

        // TODO: Replace hard-coded string literals
        IndoorResponses response = registrationService.verifyAccount(token, servletRequest);
        if(response == IndoorResponses.SUCCESS){
            headerMessage = "Success!";
            messageContent = "Your account has been verified and you're now signed in to go on as a part of our family!";
        }else if(response == IndoorResponses.TOKEN_EXPIRED){
            headerMessage = "Token Expired!";
            messageContent = "It seems that the token you used is expired. Request a new one, please.";
        }else if(response == IndoorResponses.TOKEN_ABSENT){
            headerMessage = "Invalid token!";
            messageContent = "It seems that the link you used is not correct. Please, be sure that you've not opened an old link.";
        }
        else{
            headerMessage = "Oops!...";
            messageContent = "I think you're lost. Definitely, this is not the page you're looking for...";
        }

        redirectAttributes.addFlashAttribute("header", headerMessage);
        redirectAttributes.addFlashAttribute("message", messageContent);

        return "redirect:"+PRL.redirectPageURL;
    }
}
