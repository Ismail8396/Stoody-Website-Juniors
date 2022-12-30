package com.loam.stoody.configuration.jwt;

import com.loam.stoody.global.constants.About;
import com.loam.stoody.service.user.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

import java.io.IOException;
import java.util.Enumeration;

@Component
@AllArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtility jwtUtility;
    private final CustomUserDetailsService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Auth");
        String token = null;
        String username = null;

        Enumeration<String> headerNames = request.getHeaderNames();
         // @todo just for testing will remove later
//        if (headerNames != null) {
//            while (headerNames.hasMoreElements()) {
//                System.out.println("Header: " + request.getHeader(headerNames.nextElement()));
//            }
//        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtUtility.extractUsername(token);

            // TODO: aleemkhowaja, use this instead as it is a static method?:
            // username = JWTUtility.extractUsername(token);
        }
        //if username is not and security authentication is null
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("login with username:{}", username);
            //get userDetail based on username from db
            UserDetails userDetails = userService.loadUserByUsername(username);

            //if received token is valid /not-expired then add token with security context
            if (JWTUtility.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        //do the filter
        filterChain.doFilter(request, response);

    }
}
