package com.fhd.fileUploadService.exception;

public class FileNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2639549622419279039L;

	
	public FileNotFoundException(String msg) {
		super(msg);
	}
	public FileNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}
}
