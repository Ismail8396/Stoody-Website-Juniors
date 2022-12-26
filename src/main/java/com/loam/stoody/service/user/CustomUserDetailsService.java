/*
@fileName:  CustomUserDetailsService

@aka:       CustomUserDetailsService

@purpose:   The UserDetailsService is a core interface in Spring Security framework,
            which is used to retrieve the user's authentication and authorization information.
            This interface has only one method named loadUserByUsername()
            which we can implement to feed the customer information to the Spring security API.

@hint:      The class that extends the WebSecurityConfigurerAdapter will have this class as a field, so it may be
            used in security configuration.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.service.user;

import com.amazonaws.util.StringUtils;
import com.loam.stoody.global.constants.IndoorResponses;
import com.loam.stoody.model.user.CustomUserDetails;
import com.loam.stoody.model.user.Role;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.requests.LoginRequest;
import com.loam.stoody.repository.user.LoginRequestRepository;
import com.loam.stoody.repository.user.UserRepository;
import com.loam.stoody.service.communication.sms.SmsSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRequestRepository loginRequestRepository;
    @Autowired
    private SmsSenderService smsSenderService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return user.map(CustomUserDetails::new).get();
    }

    public User loadUserByUsername(String username, String password) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        User user = userOptional.get();
        if (password.equals(user.getPassword()))
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());
//        if (!passwordEncoder.encode(password).equals(userOptional.get().getPassword()))
//            throw new AuthenticationCredentialsNotFoundException("Username or Password not matched");
            if (StringUtils.isNullOrEmpty(user.getPhoneNumber()))
                throw new RuntimeException("Mobile number not exist for OTP");
        smsSenderService.sendOTP(username, user.getPhoneNumber());
        return user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (CollectionUtils.isEmpty(roles)) throw new RuntimeException("No roles available");
        roles.stream().map(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }

    public boolean isOTPRequired(String username) {
        final long OTP_VALID_DURATION = 5 * 60 * 1000;   // 5 minutes
        LoginRequest loginRequest = loginRequestRepository.findLoginRequestByUsername(username);
        if (loginRequest.getOneTimePassword() == null) {
            return false;
        }

        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = loginRequest.getOtpRequestedTime()
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
            // OTP expires
            return false;
        }
        return true;
    }

    public IndoorResponses doesUserExist(String username, String email) {
        IndoorResponses response = IndoorResponses.SUCCESS;

        if (userRepository.findUserByEmail(email).isPresent())
            response = IndoorResponses.EMAIL_EXIST;

        if (userRepository.findUserByUsername(username).isPresent())
            response = (response == IndoorResponses.EMAIL_EXIST) ? IndoorResponses.USERNAME_EMAIL_EXIST : IndoorResponses.USERNAME_EXIST;

        return response;
    }

    public IndoorResponses createUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception ignored) {
            return IndoorResponses.FAIL;
        }

        return IndoorResponses.SUCCESS;
    }

}
