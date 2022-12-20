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

package com.loam.stoody.service.user_service;

import com.loam.stoody.model.user_models.CustomUserDetails;
import com.loam.stoody.model.user_models.User;
import com.loam.stoody.repository.user_repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        user.orElseThrow(()-> new UsernameNotFoundException("User not found!"));

        return user.map(CustomUserDetails::new).get();
    }
}
