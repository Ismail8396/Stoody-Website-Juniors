package com.loam.stoody.configuration;

import com.loam.stoody.service.CustomUserDetailService;
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
import java.security.Principal;

// In Spring Security, sometimes it is necessary to check if an authenticated user has a specific role.
// This can be useful to enable or disable particular features in our applications:
// 1. @PreAuthorize annotation (Note: @EnableGlobalMethodSecurity should be added annotation to any configuration class!)
// 2. SecurityContext (This approach will not work if we use the global context holder mode in Spring Security.)
// 3. UserDetailsService
// 4. Servlet Request

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
    @Autowired
    private CustomUserDetailService customUserDetailService;


    class LoginPageFilter extends GenericFilterBean {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            if (SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                    && ( ((HttpServletRequest)request).getRequestURI().equals("/login") ||
                         ((HttpServletRequest)request).getRequestURI().equals("/register"))
            ) {
                System.out.println("User is authenticated but trying to access login/register page, redirecting to /");
                ((HttpServletResponse)response).sendRedirect("/");
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
                .antMatchers("/", "/register", "/h2-console/**", "/api/**").permitAll()
                // Only for authorized users
                .antMatchers("/profile/*").access("hasRole('USER') or hasRole('TEACHER') or hasRole('ADMIN')")
                // Only for Admins
                .antMatchers("/admin-control-panel/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                // Login Configuration
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/")
                .usernameParameter("username")
                .passwordParameter("password")
                // OAuth2 Login/Register Configuration
                .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(googleOAuth2SuccessHandler)
                // Logout Configuration
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
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
        auth.userDetailsService(customUserDetailService);
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
