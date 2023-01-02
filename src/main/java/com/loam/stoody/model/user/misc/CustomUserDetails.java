/*
@fileName:  CustomUserDetail

@aka:       CustomUserDetail

@purpose:   Implements UserDetails methods to customize the output they might give.

@hint:      Provides core user information.
            Implementations are not used directly by Spring Security for security purposes.
            They simply store user information which is later encapsulated into Authentication objects.
            This allows non-security related user information (such as email addresses, telephone numbers etc)
            to be stored in a convenient location.

            Concrete implementations must take particular care to ensure the non-null contract detailed for each method is enforced.
            See User for a reference implementation (which you might like to extend or use in your code).

            https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetails.html

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.user.misc;

import com.loam.stoody.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends User implements UserDetails {

    // TODO: aleemkhowaja, did you consider this line when you removed the User model constructor?
    public CustomUserDetails(User user) {   super(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (super.getRoles() != null)
            super.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
        else
            System.err.println("super.getRoles() == null at "+ Thread.currentThread().getStackTrace()[1]);

        return authorities;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override//?
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !super.isAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !super.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !super.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return super.isAccountEnabled();
    }
}
