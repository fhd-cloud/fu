package com.fhd.fileUploadService.storage;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StorageService {

	public String store(MultipartFile file);

	public Resource loadFile(String fileName);

	public List<String> getAllFileNames();

	public void deleteAll();

	public boolean delete(String filename);

	public void init(String... arg);
}