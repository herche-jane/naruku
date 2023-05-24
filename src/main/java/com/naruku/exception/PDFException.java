package com.naruku.exception;

/**
 * PDF发生异常时候抛出
 */
public class PDFException  extends RuntimeException {
	public PDFException() {
		super();
	}
	
	public PDFException(String message) {
		super(message);
	}
	
	public PDFException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PDFException(Throwable cause) {
		super(cause);
	}
	
	protected PDFException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
