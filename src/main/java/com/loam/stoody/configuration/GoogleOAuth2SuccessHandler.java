package com.loam.stoody.configuration;

import com.loam.stoody.model.user_models.Role;
import com.loam.stoody.model.user_models.User;
import com.loam.stoody.repository.user_repo.RoleRepository;
import com.loam.stoody.repository.user_repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public GoogleOAuth2SuccessHandler(RoleRepository roleRepository, UserRepository userRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Authentication authentication) throws IOException, jakarta.servlet.ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

        String email = oAuth2AuthenticationToken.getPrincipal().getAttributes().get("email").toString();
        if(userRepository.findUserByEmail(email).isPresent()) {

        }else{
            User user = new User();
            user.setFirstName(oAuth2AuthenticationToken.getPrincipal().getAttributes().get("given_name").toString());
            user.setLastName(oAuth2AuthenticationToken.getPrincipal().getAttributes().get("family_name").toString());
            user.setEmail(email);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
            user.setUsername("User-"+ LocalDateTime.now().format(formatter)); // TODO: OrkhanGG check validity
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findBySearchKey("USER").get(0));
            user.setRoles(roles);
            userRepository.save(user);
        }

        redirectStrategy.sendRedirect(request,response,"/");
    }
}














