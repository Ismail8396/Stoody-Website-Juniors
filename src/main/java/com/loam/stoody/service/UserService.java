package com.loam.stoody.service;

import com.loam.stoody.model.LanguageModel;
import com.loam.stoody.model.Role;
import com.loam.stoody.model.User;
import com.loam.stoody.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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

    public List<User> getAllTeachers(){
        List<User> teacherList = new ArrayList<>();
        for(User user : userRepository.findAll()){
            for(Role role : user.getRoles()){
                if(role.getName().equals("ROLE_TEACHER")){
                    teacherList.add(user);
                    break;
                }
            }
        }
        return teacherList;
    }

    public List<User> getAllStudents(){
        List<User> studentList = new ArrayList<>();
        for(User user : userRepository.findAll()){
            for(Role role : user.getRoles()){
                if(role.getName().equals("ROLE_STUDENT")){
                    studentList.add(user);
                    break;
                }
            }
        }
        return studentList;
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

    public Optional<User> _findUserById(int id){
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
