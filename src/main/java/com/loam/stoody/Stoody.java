package com.loam.stoody;

import com.loam.stoody.service.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class Stoody {

	// Testing S3Service
//	@Autowired
//	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(Stoody.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void onStart() throws IOException {
//
//		MultipartFile fichier = new MockMultipartFile("F:/s.txt",
//				"s.txt",
//				"text/plain",
//				"This is a dummy wetwtwetwetwetewtewtfile content".getBytes(StandardCharsets.UTF_8));
//		s3Service.uploadFile(fichier);
//
//		System.out.printf("Well done");
//	}

}
