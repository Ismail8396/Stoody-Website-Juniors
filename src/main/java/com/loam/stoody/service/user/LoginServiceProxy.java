package com.loam.stoody.service.user;

import com.loam.stoody.configuration.jwt.JWTUtility;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.model.user.User;
import com.loam.stoody.service.communication.sms.SmsSenderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoginServiceProxy {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final SmsSenderService smsSenderService;

    public OutdoorResponse<?> login(String username, String password, HttpServletRequest request) {
        // Check if user exists
        try {
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

                request.getSession().setAttribute("userName", userDetails.getUsername());
                User user = customUserDetailsService.getUserByUsername(userDetails.getUsername());

                // Check whether we should send OTP or not.
                if (user.getPhoneNumber() != null)
                    if (!user.getPhoneNumber().isBlank()) {
                        if (user.isMultiFactorAuth()) {
                            // Account is Multi-Factor Authentication enabled, send an OTP to user.
                            smsSenderService.sendOTP(username, user.getPhoneNumber());
                            return new OutdoorResponse<>(IndoorResponse.OTP_REQUIRED_AND_SENT, "OTP request sent");
                        }
                    }
                // No Multi-Factor Authentication was needed, user signed in successfully.
                return new OutdoorResponse<>(IndoorResponse.SUCCESS, JWTUtility.generateToken(username));
            } catch (AuthenticationException ignore) {
                return new OutdoorResponse<>(IndoorResponse.FAIL, "Authentication fail");
            }
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "Bad Credentials");
        }
    }

    public OutdoorResponse<?> verifyOTP(String token, String username) {

        // TODO: Add token expired response?...

        if (smsSenderService.validateOTP(token, username)) {
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, JWTUtility.generateToken(username));
            //return new OutdoorResponse<>(IndoorResponse.SUCCESS, new JwtResponse(JWTUtility.generateToken(username), null, username, null, null));
        }
        return new OutdoorResponse<>(IndoorResponse.FAIL, "Invalid request");
    }
}
