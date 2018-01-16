package com.fhd.fileUploadService.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileResponse {

	public final String id;
	public final String url;
	public final String fileName;
	public final ErrorMessage error;
    
	@JsonCreator
	public FileResponse(@JsonProperty("id") String id,
			@JsonProperty("fileName") String fileName,
			@JsonProperty("url") String url,
			@JsonProperty("error") ErrorMessage error) {
		this.id = id;
		this.fileName = fileName;
		this.url = url;
		this.error = error;
		
	}
}
