/*
@fileName:  Stoody

@aka:       Stoody Web Application Entry Point

@purpose:   This class automatically creates the ApplicationContext from the classpath,
 			scan the configuration classes and launch the application.

@author:    Spring Initializer

@created:   01.12.2022
*/

package com.loam.stoody;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.global.constants.ProjectConfigurationVariables;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.global.managers.ClassScanner;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.UserFollowers;
import com.loam.stoody.model.user.misc.Role;
import com.loam.stoody.repository.user.RoleRepository;
import com.loam.stoody.repository.user.attributes.UserFollowersRepository;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories(basePackages = "com.*")
@AllArgsConstructor
public class Stoody {
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserFollowersRepository userFollowersRepository;
    private final RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(Stoody.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        // Only for development
        if(ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)) {
            Class<?>[] classes = ClassScanner
                    .findAllAnnotatedClassesInPackage("com.loam.stoody", UnderDevelopment.class);
            StringBuilder classesList = new StringBuilder();
            Arrays.stream(classes).forEach(e -> classesList.append("Package------>").append(e.getPackage()).append(":\nClass name------>").append(e.getSimpleName()).append("\n--------------------------------\n"));
            StoodyLogger.DebugLog(ConsoleColors.YELLOW, "Found " + classes.length + " @UnderDevelopment annotated entities:\n" + classesList);
        }

        //--------------------------------
        // TODO: REMOVE LATER
        try {
            if (roleRepository.count() <= 0) {
                Role testRole = new Role();
                testRole.setName("ROLE_USER");
                roleRepository.save(testRole);
                Role testRole1 = new Role();
                testRole1.setName("ROLE_ADMIN");
                roleRepository.save(testRole1);

                Role testRole2 = new Role();
                testRole2.setName("ROLE_INSTRUCTOR");
                roleRepository.save(testRole2);
            }
            System.out.println(roleRepository.findAll());
            //--------------------------------

            User user = customUserDetailsService.getDefaultUser();
            user.setEmail("stoody.org@gmail.com");
            user.setUsername("Stoody");
            user.setPassword(bCryptPasswordEncoder.encode("123"));

            user.setRoles(roleRepository.findAll().stream()
                    .filter(e->e.getName().equals("ROLE_ADMIN")).toList());

            System.out.println("Stoody Roles: "+user.getRoles());
            customUserDetailsService.saveUser(user);

            User user2 = customUserDetailsService.getDefaultUser();
            user2.setEmail("orxan.eliyev.orxan@gmail.com");
            user2.setUsername("OrkhanGG");
            user2.setPassword(bCryptPasswordEncoder.encode("123"));
            user2.setRoles(roleRepository.findAll().stream()
                    .filter(e->e.getName().equals("ROLE_INSTRUCTOR")).toList());
            customUserDetailsService.saveUser(user2);

            UserFollowers userFollowers = new UserFollowers();
            userFollowers.setFrom(user2);
            userFollowers.setTo(user);
            userFollowersRepository.save(userFollowers);

            UserFollowers userFollowers1 = new UserFollowers();
            userFollowers1.setFrom(user);
            userFollowers1.setTo(user2);
            userFollowersRepository.save(userFollowers1);
        } catch (Exception ex) {
            System.out.println("ERRROR while insert user details in db");
        }

    }
}
