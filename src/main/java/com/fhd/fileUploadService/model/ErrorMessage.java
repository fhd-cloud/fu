package com.fhd.fileUploadService.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
	public final int id;
	public final String message;
	@JsonCreator
	public ErrorMessage(@JsonProperty("id") int id,
			@JsonProperty("message") String message) {
		this.id = id;
		this.message = message;
	}
}
