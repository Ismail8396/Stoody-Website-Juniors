package com.loam.stoody.service;

import com.loam.stoody.model.CustomUserDetail;
import com.loam.stoody.model.User;
import com.loam.stoody.repository.CategoryRepository;
import com.loam.stoody.repository.CourseRepository;
import com.loam.stoody.repository.UserRepository;
import com.loam.stoody.repository.VideoRepository;
import com.loam.stoody.service.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        user.orElseThrow(()-> new UsernameNotFoundException("User not found!"));

        return user.map(CustomUserDetail::new).get();
    }
}
