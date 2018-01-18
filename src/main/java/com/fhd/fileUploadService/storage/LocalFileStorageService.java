package com.fhd.fileUploadService.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fhd.fileUploadService.exception.FileNotFoundException;
import com.fhd.fileUploadService.exception.FileUploadException;

@Service
public class LocalFileStorageService implements StorageService {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private Path fileStorePath;

	public String store(MultipartFile file) {
		String fileName = null;
		try {
			fileName = file.getOriginalFilename();
			Files.copy(file.getInputStream(), this.fileStorePath.resolve(fileName));
		} catch (Exception e) {
			throw new FileUploadException("Unable to store file ", e).withFile(fileName);
		}
		return file.getOriginalFilename();
	}

	public Resource loadFile(String fileName) {
		try {
			Path file = this.fileStorePath.resolve(fileName);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() && resource.isReadable()) {
				return resource;
			} else {
				throw new FileNotFoundException(String.format("Unable to get the file : %s ", fileName));
			}
		} catch (MalformedURLException e) {
			throw new FileUploadException("Invalid filename to form valid url", e).withFile(fileName);
		}
	}

	public List<String> getAllFileNames() {
		try {
			return Files.list(fileStorePath).map(file -> file.getFileName().toString()).collect(Collectors.toList());
		} catch (Exception e) {
			throw new FileUploadException("Unable to get all fileNames", e);
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(fileStorePath.toFile());
	}

	public boolean delete(String filename) {
		try {
			Path file = this.fileStorePath.resolve(filename);
			return Files.deleteIfExists(file);
		} catch (IOException e) {
			throw new FileUploadException(String.format("Could not delete file %s", filename), e);
		}
	}

	public void init(String... arg) {
		try {
			String uploadDestination = null;
			if (arg.length > 0) {
				uploadDestination = arg[0];
			} else {
				// default location
				uploadDestination = "upload-files-store";
			}
			fileStorePath = Paths.get(uploadDestination);
			if (!Files.isDirectory(fileStorePath)) {
				Files.createDirectory(fileStorePath);
				log.info("fileStorePath {} created", fileStorePath.toString());
			} else {
				log.info("fileStorePath {} exist", fileStorePath.toString());
			}
		} catch (IOException e) {
			throw new FileUploadException("Could not initialize storage!", e);
		}
	}
}
