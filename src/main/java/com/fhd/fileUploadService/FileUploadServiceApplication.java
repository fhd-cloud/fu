package com.fhd.fileUploadService;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fhd.fileUploadService.storage.StorageService;

@SpringBootApplication
public class FileUploadServiceApplication implements CommandLineRunner {

	@Resource
	StorageService storageService;
	
	public static void main(String[] args) {
		SpringApplication.run(FileUploadServiceApplication.class, args);
	}
	
	@Override
	public void run(String... arg) throws Exception {
		storageService.init(arg);
	}
}
