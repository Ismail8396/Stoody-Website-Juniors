package com.loam.stoody.configuration.jwt;

import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.service.user.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtility jwtUtility;
    private final CustomUserDetailsService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie authCookie = WebUtils.getCookie(request, "Auth");

        String jwtTokenValue = authCookie == null ? null : authCookie.getValue();
        try {
            if (jwtTokenValue == null) {
                StoodyLogger.DebugLog(ConsoleColors.YELLOW, "JWT TOKEN WAS NULL!");
                throw new RuntimeException("Test");
            }
            StoodyLogger.DebugLog(ConsoleColors.YELLOW, "PASSED JWT!");

            String username = JWTUtility.extractUsername(jwtTokenValue);
            if (username == null) {
                StoodyLogger.DebugLog(ConsoleColors.YELLOW, "USERNAME WAS NULL");

                throw new RuntimeException("Test");
            }

            StoodyLogger.DebugLog(ConsoleColors.YELLOW, "USERNAME PASSED");

            // This can throw UsernameNotFoundException
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (userDetails == null) {
                StoodyLogger.DebugLog(ConsoleColors.YELLOW, "USER DETAILS WAS NULL");
                throw new RuntimeException("Test");
            }
            StoodyLogger.DebugLog(ConsoleColors.YELLOW, "USER DETAILS PASSED");

            if (JWTUtility.validateToken(jwtTokenValue, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                StoodyLogger.DebugLog(ConsoleColors.YELLOW, "PASSED VALIDATION");

            } else{
                StoodyLogger.DebugLog(ConsoleColors.YELLOW, "VALIDATION ERROR!");
                throw new RuntimeException("Test");
            }

        } catch (RuntimeException ignore) {
        }

        //do the filter
        filterChain.doFilter(request, response);

    }
}