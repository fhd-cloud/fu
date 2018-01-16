package com.fhd.fileUploadService.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

//Sorts by method name
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StorageServiceTest {

	MockMultipartFile file;
	StorageService target;
	String testFileName = "test-file.txt";
	static Path testStorePath;

	@BeforeClass
	public static void setupStore() {
		UUID tmpFolderSuffix = UUID.randomUUID();
		testStorePath = Paths.get("test-store-" + tmpFolderSuffix);
	}

	@Before
	public void setup() throws IOException {

		target = new StorageService();
		target.init(testStorePath.toString());
		file = new MockMultipartFile("file", testFileName, MediaType.TEXT_PLAIN_VALUE, "Testing".getBytes());
	}

	@AfterClass
	public static void cleanup() throws IOException {
		FileSystemUtils.deleteRecursively(testStorePath.toFile());

	}

	@Test
	public void A_store() throws Exception {

		String output = target.store(file);
		assertThat(testStorePath.resolve(testFileName)).exists();
		assertThat(output).isEqualTo(testFileName);

	}

	@Test
	public void B_loadFile() throws Exception {
		Resource output = target.loadFile(testFileName);
		assertThat(output).isNotNull();

	}

	@Test
	public void C_getAllFileNames() {

		List<String> files = target.getAllFileNames();
		assertThat(files).contains(testFileName);
		assertThat(files.size()).isEqualTo(1);
	}

	@Test
	public void D_deleteFile() throws Exception {
		boolean output = target.delete(testFileName);
		assertThat(output).isTrue();
	}

	@Test
	public void E_getAllNamesAfterDelete() {
		List<String> files = target.getAllFileNames();
		assertThat(files).doesNotContain(testFileName);
		assertThat(files.size()).isEqualTo(0);
	}
}
