package com.fhd.fileUploadService.exception;

public class FileUploadException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2639549622419279041L;

	private String fileName;
	public FileUploadException(String msg) {
		super(msg);
	}
	public FileUploadException(String msg, Throwable e) {
		super(msg, e);
	}
	public FileUploadException withFile(String fileName) {
		this.fileName = fileName;
		return this;
	}
	
	public String getFileName() {
		return fileName;
	}
}
