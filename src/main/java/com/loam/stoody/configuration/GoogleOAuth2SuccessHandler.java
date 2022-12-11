package com.loam.stoody.configuration;

import com.loam.stoody.model.Role;
import com.loam.stoody.model.User;
import com.loam.stoody.repository.RoleRepository;
import com.loam.stoody.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

        String email = oAuth2AuthenticationToken.getPrincipal().getAttributes().get("email").toString();
        if(userRepository.findUserByEmail(email).isPresent()) {

        }else{
            User user = new User();
            user.setFirstName(oAuth2AuthenticationToken.getPrincipal().getAttributes().get("given_name").toString());
            user.setLastName(oAuth2AuthenticationToken.getPrincipal().getAttributes().get("family_name").toString());
            user.setEmail(email);
            user.setUsername(email); // TODO: OrkhanGG check validity
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findById(2).get());
            user.setRoles(roles);
            userRepository.save(user);
        }

        redirectStrategy.sendRedirect(request,response,"/");
    }
}














