package com.fhd.fileUploadService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fhd.fileUploadService.model.ErrorMessage;
import com.fhd.fileUploadService.model.FileResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(FileNotFoundException.class)
	public final ResponseEntity<FileResponse> handleFileNotFoundException(FileNotFoundException ex,
			WebRequest request) {
		ErrorMessage error = new ErrorMessage(ErrorCodes.FILE_NOT_FOUND, ex.getMessage());
		FileResponse response = new FileResponse(null, null, null, error);
		return new ResponseEntity<FileResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FileUploadException.class)
	public final ResponseEntity<FileResponse> handleFileUploadException(FileUploadException ex, WebRequest request) {
		ErrorMessage error = new ErrorMessage(ErrorCodes.STORAGE_ERROR, ex.getMessage());
		FileResponse response = new FileResponse(null, ex.getFileName(), null, error);
		return new ResponseEntity<FileResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
