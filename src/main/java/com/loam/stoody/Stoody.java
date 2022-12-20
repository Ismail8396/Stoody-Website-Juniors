/*
@fileName:  Stoody

@aka:       Stoody Web Application Entry Point

@purpose:   This class automatically creates the ApplicationContext from the classpath,
 			scan the configuration classes and launch the application.

@author:    Spring Initializer

@created:   01.12.2022
*/

package com.loam.stoody;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories(basePackages="com.*")
public class Stoody {
	public static void main(String[] args) {
		SpringApplication.run(Stoody.class, args);
	}
}
