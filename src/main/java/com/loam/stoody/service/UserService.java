package com.loam.stoody.service;

import com.loam.stoody.model.User;
import com.loam.stoody.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> _findUserByUsername(String username){
        return userRepository.findUserByUsername(username);
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
}
