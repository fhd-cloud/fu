package com.fhd.fileUploadService;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fhd.fileUploadService.controller.FileUploadController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUploadServiceApplicationTests {

	@Autowired
	private FileUploadController controller;

	@Before
	public void setup() {

	}

	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();

	}

}
