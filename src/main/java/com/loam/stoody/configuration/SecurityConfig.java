package com.loam.stoody.configuration;

import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.user.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// In Spring Security, sometimes it is necessary to check if an authenticated user has a specific role.
// This can be useful to enable or disable particular features in our applications:
// 1. @PreAuthorize annotation (Note: @EnableGlobalMethodSecurity should be added annotation to any configuration class!)
// 2. SecurityContext (This approach will not work if we use the global context holder mode in Spring Security.)
// 3. UserDetailsService
// 4. Servlet Request

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
    @Autowired
    public SecurityConfig(GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler){
        this.googleOAuth2SuccessHandler = googleOAuth2SuccessHandler;
    }

    static class LoginPageFilter extends GenericFilterBean {
        @Override
        public void doFilter(jakarta.servlet.ServletRequest servletRequest, jakarta.servlet.ServletResponse servletResponse, jakarta.servlet.FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {
            if (SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                    && ( ((HttpServletRequest)servletRequest).getRequestURI().equals(PRL.signInURL) ||
                    ((HttpServletRequest)servletRequest).getRequestURI().equals(PRL.signUpURL))
            ) {
                System.out.println("User is authenticated but trying to access signIn/signUp page, redirecting to home");
                ((HttpServletResponse)servletResponse).sendRedirect(PRL.homeURL);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new LoginPageFilter(), DefaultLoginPageGeneratingFilter.class);

        http
                .authorizeHttpRequests()

                // For Visitors
                .requestMatchers(PRL.homeURL, PRL.signUpURL+"/**", PRL.redirectPageURL+"/**").permitAll()

                // Only for authorized users
                .requestMatchers(""/* PAGES SPECIAL FOR AUTHENTICATED USERS */)
                .hasRole("USER")

                // Only for Admins
                .requestMatchers(""/* PAGES SPECIAL FOR ADMIN */).hasRole("ADMIN")
                .anyRequest()
                .authenticated()

                // Login Configuration
                .and()
                .formLogin()
                .loginPage(PRL.signInURL)
                .permitAll()
                .failureUrl(PRL.signInURL + "?error=true")
                .defaultSuccessUrl(PRL.homeURL)
                .usernameParameter("username")
                .passwordParameter("password")

                // OAuth2 SignIn/SignUp Configuration
                .and()
                .oauth2Login()
                .loginPage(PRL.signInURL)
                .successHandler(googleOAuth2SuccessHandler)

                // Logout Configuration
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(PRL.logoutURL))
                .logoutSuccessUrl(PRL.signInURL)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")

                // Misc
                .and()
                .exceptionHandling()
                .and()
                .csrf()
                .disable();

        http.headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/resources/**",
                "/assets/**",
                "/assets/css/**",
                "/assets/js/**",
                "/assets/js/vendors/**",
                "/assets/images/**",
                "/assets/fonts/**",
                "/assets/fonts/feather/**",
                "/assets/fonts/feather/fonts/**",
                "/assets/libs/**");
    }
}
