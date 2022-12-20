package com.loam.stoody.configuration;

import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.user_service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// In Spring Security, sometimes it is necessary to check if an authenticated user has a specific role.
// This can be useful to enable or disable particular features in our applications:
// 1. @PreAuthorize annotation (Note: @EnableGlobalMethodSecurity should be added annotation to any configuration class!)
// 2. SecurityContext (This approach will not work if we use the global context holder mode in Spring Security.)
// 3. UserDetailsService
// 4. Servlet Request

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler, CustomUserDetailsService customUserDetailsService){
        this.googleOAuth2SuccessHandler = googleOAuth2SuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
    }

    static class LoginPageFilter extends GenericFilterBean {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            if (SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                    && ( ((HttpServletRequest)request).getRequestURI().equals(PRL.signInURL) ||
                         ((HttpServletRequest)request).getRequestURI().equals(PRL.signUpURL))
            ) {
                System.out.println("User is authenticated but trying to access signIn/signUp page, redirecting to home");
                ((HttpServletResponse)response).sendRedirect(PRL.homeURL);
            }
            chain.doFilter(request, response);
        }

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(
                new LoginPageFilter(), DefaultLoginPageGeneratingFilter.class);

        http
                .authorizeRequests()
                // For Visitors
                .antMatchers(PRL.homeURL, PRL.signUpURL).permitAll()
                // Only for authorized users
                .antMatchers(/* PAGES SPECIAL FOR AUTHENTICATED USERS */).access("hasRole('USER') or hasRole('TEACHER') or hasRole('ADMIN')")
                // Only for Admins
                .antMatchers(/* PAGES SPECIAL FOR ADMIN */).hasRole("ADMIN")
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
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
        auth.eraseCredentials(false);
//        auth.inMemoryAuthentication()
//                .withUser("root").password(passwordEncoder().encode("root")).roles("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
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
