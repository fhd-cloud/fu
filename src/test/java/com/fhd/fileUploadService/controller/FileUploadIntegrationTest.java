package com.fhd.fileUploadService.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileUploadIntegrationTest {

	@Autowired
    private TestRestTemplate restTemplate;

	@Test
	public void create( ) {
//		MockMultipartFile file = new MockMultipartFile("file", testFileName, MediaType.IMAGE_PNG_VALUE,
//				"Testing".getBytes());
//		
//		ResponseEntity<FileResponse> responseEntity =
//	            restTemplate.postForEntity("/clients", new CreateClientRequest("Foo"), Client.class);
//	        Client client = responseEntity.getBody();
//	        
//	        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//	        assertEquals("Foo", client.getName());
	}
}
