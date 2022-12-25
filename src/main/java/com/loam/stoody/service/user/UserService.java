package com.loam.stoody.service.user;

import com.loam.stoody.model.user.Role;
import com.loam.stoody.model.user.User;
import com.loam.stoody.repository.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Deprecated
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> getAllStaff(){
        List<User> staffList = new ArrayList<>();
        for(User user : userRepository.findAll()){
            for(Role role : user.getRoles()){
                if(role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_MODERATOR")){
                    staffList.add(user);
                    break;
                }
            }
        }
        return staffList;
    }

    public Optional<User> _findUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public List<User> findAllUsersByUsernameBySearchKey(String filter){
        return userRepository.findUsernameBySearchKey(filter);
    }

    public Optional<User> _findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> _findUserById(long id){
        return userRepository.findById(id);
    }

    public boolean saveUser(User modifiedData){
        try {
            userRepository.save(modifiedData);
        }catch (IllegalArgumentException exception){
            System.err.println(String.format("User could not be saved!", exception.getMessage()));
            return false;
        }
        return true;// TODO: this is not supposed to be true always
    }

    public User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return _findUserByUsername(auth.getName()).orElse(null);
    }
}
