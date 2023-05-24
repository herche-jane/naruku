package com.naruku.exception;

public class UnkownDataSourceException extends RuntimeException{
	public UnkownDataSourceException() {
	}
	
	public UnkownDataSourceException(String message) {
		super(message);
	}
	
	public UnkownDataSourceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnkownDataSourceException(Throwable cause) {
		super(cause);
	}
	
	public UnkownDataSourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
