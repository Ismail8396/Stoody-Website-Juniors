package com.loam.stoody.service.user;

import com.loam.stoody.enums.UserRoles;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.misc.Role;
import com.loam.stoody.repository.user.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRoleService {
    public UserRoles getUserMostPrioritizedRole(User user){
        if(user != null){
            List<Role> userRoles = user.getRoles();
            if(!ObjectUtils.isEmpty(userRoles)){
                if(userRoles.stream().anyMatch(e-> e.getName().equals(UserRoles.ROLE_ADMIN.toString())) ){
                    return UserRoles.ROLE_ADMIN;
                }else if(userRoles.stream().anyMatch(e-> e.getName().equals(UserRoles.ROLE_MODERATOR.toString()))){
                    return UserRoles.ROLE_MODERATOR;
                }else if(userRoles.stream().anyMatch(e-> e.getName().equals(UserRoles.ROLE_INSTRUCTOR.toString()))){
                    return UserRoles.ROLE_INSTRUCTOR;
                }
            }
        }
        return UserRoles.ROLE_USER;
    }
}
