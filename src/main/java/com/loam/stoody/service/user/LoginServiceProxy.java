package com.loam.stoody.service.user;

import com.amazonaws.util.StringUtils;
import com.loam.stoody.configuration.jwt.JWTUtility;
import com.loam.stoody.configuration.jwt.JwtResponse;
import com.loam.stoody.dto.api.request.LoginResponse;
import com.loam.stoody.model.user.User;
import com.loam.stoody.service.communication.sms.SmsSenderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoginServiceProxy {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final SmsSenderService smsSenderService;

    public ResponseEntity<?> login(String username, String password, HttpServletRequest request) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            request.getSession().setAttribute("userName", userDetails.getUsername());
            User user = customUserDetailsService.getUserByUsername(userDetails.getUsername());
            //send otp with register number
            if(StringUtils.isNullOrEmpty(user.getPhoneNumber()))
                throw new RuntimeException(String.format("Mobile number is null with registered user: %s", username));
            smsSenderService.sendOTP(username, user.getPhoneNumber());
            return ResponseEntity.ok(new LoginResponse(userDetails.getUsername(),"Login Successfully, OTP sent to register number please verify"));
        } catch (Exception ex) {
            throw new RuntimeException("Invalid Username or Password");
        }
    }

    public ResponseEntity<?> verifyOTP(String token, String username) {
        if(smsSenderService.validateOTP(token, username)){
            return ResponseEntity.ok(new JwtResponse(JWTUtility.generateToken(username),
                    null,
                    username,
                    null,
                    null));
        }
        return (ResponseEntity<?>) ResponseEntity.of(ProblemDetail.forStatus(HttpStatus.FORBIDDEN));
    }
}
