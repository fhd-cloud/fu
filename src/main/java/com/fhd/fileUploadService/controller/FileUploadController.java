package com.fhd.fileUploadService.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.fhd.fileUploadService.exception.ErrorCodes;
import com.fhd.fileUploadService.model.ErrorMessage;
import com.fhd.fileUploadService.model.FileResponse;
import com.fhd.fileUploadService.storage.StorageService;

@RestController
@RequestMapping(value = "/api/files")
public class FileUploadController {

	@Autowired
	StorageService storageService;
	
	@Value("${custom.http.multipart.mimeType.accept:^image/.*$}")        // Defaulted to ^image/.*$ (images Only)
    private String acceptableContentType;

	@PostMapping("")
	public ResponseEntity<FileResponse> create(@RequestParam("file") MultipartFile file) {
		FileResponse response = saveFile(file);
		if (response.error == null) {
			return new ResponseEntity<FileResponse>(response, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<FileResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("")
	public @ResponseBody List<FileResponse> getAll(Model model) {

		List<FileResponse> files = getAllAvailableFiles().stream()
				.map(fileName -> new FileResponse(fileName, fileName, createURL(fileName), null))
				.collect(Collectors.toList());

		return files;
	}

	@GetMapping(value = "/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.loadFile(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping("/bulkUpload")
	public ResponseEntity<List<FileResponse>> bulkUpload(@RequestParam("file") MultipartFile[] files) {

		List<FileResponse> filesResposes = new ArrayList<FileResponse>(files.length);
		for (MultipartFile file : files) {
			filesResposes.add(saveFile(file));
		}
		long failedCount = filesResposes.stream().filter(resp -> resp.error != null).count();
		if (failedCount == 0) {
			return new ResponseEntity<List<FileResponse>>(filesResposes, HttpStatus.CREATED);
		} else if (failedCount == files.length) {
			return new ResponseEntity<List<FileResponse>>(filesResposes, HttpStatus.METHOD_FAILURE);
		} else {
			return new ResponseEntity<List<FileResponse>>(filesResposes, HttpStatus.MULTI_STATUS);
		}
	}

	private FileResponse saveFile(MultipartFile file) {
		FileResponse response;
		if (file.getContentType().matches(acceptableContentType)) {

			String id = storageService.store(file);
			String fileName = file.getOriginalFilename();
			response = new FileResponse(id, fileName, createURL(fileName), null);
		} else {
			ErrorMessage error = new ErrorMessage(ErrorCodes.INVALID_FILE,
					String.format("Invalid file format %s ", file.getContentType()));
			response = new FileResponse(null, file.getOriginalFilename(), null, error);

		}
		return response;
	}

	private List<String> getAllAvailableFiles() {
		return storageService.getAllFileNames();

	}

	private String createURL(String fileName) {
		return MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "getFile", fileName).build()
				.toString();
	}
}
