/*
@fileName:  Stoody

@aka:       Stoody Web Application Entry Point

@purpose:   This class automatically creates the ApplicationContext from the classpath,
 			scan the configuration classes and launch the application.

@author:    Spring Initializer

@created:   01.12.2022
*/

package com.loam.stoody;

import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.UserFollowers;
import com.loam.stoody.model.user.misc.Role;
import com.loam.stoody.repository.user.RoleRepository;
import com.loam.stoody.repository.user.attributes.UserFollowersRepository;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories(basePackages="com.*")
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
		//--------------------------------
		// TODO: REMOVE LATER
		if(roleRepository.count() <= 0)
		{
			Role testRole = new Role();
			testRole.setName("ROLE_USER");
			roleRepository.save(testRole);
			Role testRole1 = new Role();
			testRole1.setName("ROLE_ADMIN");
			roleRepository.save(testRole);
		}
		System.out.println(roleRepository.findAll());
		//--------------------------------

		User user = customUserDetailsService.getDefaultUser();
		user.setEmail("stoody.org@gmail.com");
		user.setUsername("Stoody");
		user.setPassword(bCryptPasswordEncoder.encode("123"));
		customUserDetailsService.saveUser(user);

		User user2 = customUserDetailsService.getDefaultUser();
		user2.setEmail("orxan.eliyev.orxan@gmail.com");
		user2.setUsername("OrkhanGG");
		user2.setPassword(bCryptPasswordEncoder.encode("123"));
		customUserDetailsService.saveUser(user2);

		UserFollowers userFollowers = new UserFollowers();
		userFollowers.setFrom(user2);
		userFollowers.setTo(user);
		userFollowersRepository.save(userFollowers);

		UserFollowers userFollowers1 = new UserFollowers();
		userFollowers1.setFrom(user);
		userFollowers1.setTo(user2);
		userFollowersRepository.save(userFollowers1);
	}
}
