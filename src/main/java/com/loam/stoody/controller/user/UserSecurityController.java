package com.loam.stoody.controller.user;

import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.model.user.User;
import com.loam.stoody.service.communication.email.EmailSenderService;
import com.loam.stoody.service.communication.sms.SmsSenderService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.utils.CacheService;
import com.loam.stoody.service.utils.RegexValidators;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SecureRandom;

@Controller
@AllArgsConstructor
public class UserSecurityController {
    private final RegexValidators regexValidators;
    private final CustomUserDetailsService customUserDetailsService;
    private final SmsSenderService smsSenderService;
    private final EmailSenderService emailSenderService;

    // -> Rest API

    // ----> Email
    @PostMapping("/user/profile/security/email/change/request")
    @ResponseBody
    public OutdoorResponse<?> requestChangeEmail(@RequestParam("email") String email) {
        if (!regexValidators.validateEmail(email))
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST,
                    "Email appears to be invalid. Please try again with the correct one.");

        try {
            final User currentUser = customUserDetailsService.getCurrentUser();
            if (currentUser == null) throw new RuntimeException();

            if (customUserDetailsService.isEmailInUse(email))
                return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "This email is already in use!");

            final Integer otp = CacheService.generateRandomOTP();
            CacheService.put(currentUser.getUsername(), otp);
            emailSenderService.sendEmail(email, "Stoody Email Change Request", "Hello!\n\n" + currentUser.getUsername() + ", a Stoody user, wishes to link his account to this e-mail address. To accept, enter the following code into the validation section:\n\n" + otp + "\n\nIf this is not you, please do not disregard this email; you most likely received it by mistake.");

            return new OutdoorResponse<>(IndoorResponse.OTP_REQUIRED_AND_SENT, "OTP sent successfully.");
        } catch (RuntimeException ignore) {
            return new OutdoorResponse<>(IndoorResponse.ACCESS_DENIED, "You are not authorized to make this request!");
        }
    }

    @PostMapping("/user/profile/security/email/verify")
    @ResponseBody
    public OutdoorResponse<?> requestVerifyEmailChange(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp) {
        if (!regexValidators.validateEmail(email))
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST,
                    "Email appears to be invalid. Please try again with the correct one.");

        try {
            final User currentUser = customUserDetailsService.getCurrentUser();
            if (currentUser == null) throw new RuntimeException();

            if (customUserDetailsService.isEmailInUse(email))
                return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "This email is already in use!");

            if (CacheService.isValid(otp, currentUser.getUsername())) {
                currentUser.setEmail(email);
                customUserDetailsService.saveUser(currentUser);
                return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
            }
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "The code you entered is incorrect, please try again.");
        } catch (RuntimeException ignore) {
            return new OutdoorResponse<>(IndoorResponse.ACCESS_DENIED, "You are not authorized to make this request!");
        }
    }

    // ----> Password
    @PostMapping("/user/profile/security/password/change")
    @ResponseBody
    public OutdoorResponse<?> requestChangePassword(@RequestParam("currentPassword") String currentPassword,
                                                    @RequestParam("newPassword") String newPassword,
                                                    @RequestParam("confirmPassword") String confirmPassword,
                                                    @RequestParam("strength") Integer strength) {
        if (currentPassword.equals(newPassword)) {
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "This is as same as the old password!");
        }

        if (!newPassword.equals(confirmPassword)) {
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "Passwords does not match!");
        }

        if(strength < 2){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "This password is quite weak! Please, try a new one.");
        }

        try {
            //final User currentUser = customUserDetailsService.getCurrentUser();
            final User currentUser = customUserDetailsService.getUserByUsername("OrkhanGG");
            if (currentUser == null) throw new RuntimeException();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());
            if(!passwordEncoder.matches(currentPassword,currentUser.getPassword())){
                return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "Current password is incorrect!");
            }

            if(!passwordEncoder.matches(newPassword, currentUser.getPassword())){
                currentUser.setPassword(passwordEncoder.encode(newPassword));
                customUserDetailsService.saveUser(currentUser);
                return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
            }
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "This password cannot be set!");
        } catch (RuntimeException ignore) {
            return new OutdoorResponse<>(IndoorResponse.ACCESS_DENIED, "You are not authorized to make this request!");
        }

    }

    // ----> MultiFactor Auth
    // ----> Phone Number
    @PostMapping("/user/profile/security/phone/change/request")
    @ResponseBody
    public OutdoorResponse<?> requestChangeNumber(@RequestParam("phoneNumber") String phoneNumber) {
        if (!phoneNumber.contains("+"))
            phoneNumber = "+" + phoneNumber;

        if (!regexValidators.validateNumber(phoneNumber))
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "Phone number appears to be invalid. Please try again with the correct one.");
        try {
            final User currentUser = customUserDetailsService.getCurrentUser();
            if (currentUser == null) throw new RuntimeException();

            if (customUserDetailsService.isPhoneNumberInUse(phoneNumber))
                return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "This phone number is already in use!");

            smsSenderService.sendOTP(currentUser.getUsername(), phoneNumber);
            return new OutdoorResponse<>(IndoorResponse.OTP_REQUIRED_AND_SENT, "OTP sent successfully.");
        } catch (RuntimeException ignore) {
            return new OutdoorResponse<>(IndoorResponse.ACCESS_DENIED, "You are not authorized to make this request!");
        }
    }

    @PostMapping("/user/profile/security/phone/verify")
    @ResponseBody
    public OutdoorResponse<?> requestVerifyPhoneNumber(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("otp") String otp) {
        if (!phoneNumber.contains("+"))
            phoneNumber = "+" + phoneNumber;

        if (!regexValidators.validateNumber(phoneNumber))
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST,
                    "Phone number appears to be invalid. Please try again with the correct one.");

        try {
            final User currentUser = customUserDetailsService.getCurrentUser();
            if (currentUser == null) throw new RuntimeException();

            if (customUserDetailsService.isPhoneNumberInUse(phoneNumber))
                return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "This phone number is already in use!");

            if (smsSenderService.validateOTP(otp, currentUser.getUsername())) {
                currentUser.setPhoneNumber(phoneNumber);
                customUserDetailsService.saveUser(currentUser);
                return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
            }
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "The code you entered is incorrect, please try again.");
        } catch (RuntimeException ignore) {
            return new OutdoorResponse<>(IndoorResponse.ACCESS_DENIED, "You are not authorized to make this request!");
        }
    }
}
