package com.fhd.fileUploadService.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fhd.fileUploadService.exception.ErrorCodes;
import com.fhd.fileUploadService.exception.FileNotFoundException;
import com.fhd.fileUploadService.model.ErrorMessage;
import com.fhd.fileUploadService.model.FileResponse;
import com.fhd.fileUploadService.storage.StorageService;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	StorageService storageService;

	static final String testFileName = "testFile.png";

	@Before
	public void setUp() throws Exception {

	}

	@AfterClass
	public static void FileSystemCleanup() throws Exception {

		Files.deleteIfExists(Paths.get(testFileName));
	}

	@Test
	public void create() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", testFileName, MediaType.IMAGE_PNG_VALUE,
				"Testing".getBytes());
		when(storageService.store(file)).thenReturn(testFileName);
		mockMvc.perform(fileUpload("/api/files").file(file)).andExpect(status().isCreated());

		verify(storageService).store(file);
	}

	@Test
	public void create_not_image() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test-file.pdf", MediaType.APPLICATION_PDF_VALUE,
				"Testing".getBytes());
		when(storageService.store(file)).thenReturn(testFileName);
		mockMvc.perform(fileUpload("/api/files").file(file)).andExpect(status().isBadRequest());

		verify(storageService, never()).store(file);
	}

	@Test
	public void getAll() throws Exception {
		List<String> filesList = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			filesList.add("TestFile-" + i);
		}
		when(storageService.getAllFileNames()).thenReturn(filesList);
		ResultActions result = mockMvc.perform(get("/api/files")).andExpect(status().isOk());

		result.andExpect(jsonPath("$.length()", is(filesList.size())));
		for (String file : filesList) {
			result.andExpect(jsonPath("$.[*].fileName", hasItem(file)));
		}
		verify(storageService).getAllFileNames();
	}

	@Test
	public void getFile() throws Exception {

		byte[] expectedBytes = "testString".getBytes();
		FileSystemResource resource = new FileSystemResource(
				Files.write(Paths.get(testFileName), expectedBytes).toFile());
		when(storageService.loadFile(testFileName)).thenReturn(resource);

		mockMvc.perform(get("/api/files/" + testFileName)).andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
						String.format("attachment; filename=\"%s\"", testFileName)))
				.andExpect(content().bytes(expectedBytes));

		verify(storageService).loadFile(testFileName);
	}
	
	@Test
	public void get_not_available() throws Exception {
		FileNotFoundException expectedException = new FileNotFoundException("File not found");
		
		when(storageService.loadFile(testFileName)).thenThrow(expectedException);
		
		mockMvc.perform(get("/api/files/" + testFileName)).andExpect(status().isNotFound());

		verify(storageService).loadFile(testFileName);
	}
}
