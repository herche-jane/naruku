package com.naruku.exception;
/**
 * 异常类
 * @author herche
 * @date 2022/10/01
 */
public class POIException extends RuntimeException {
	public POIException() {
		super();
	}
	
	public POIException(String message) {
		super(message);
	}
	
	public POIException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public POIException(Throwable cause) {
		super(cause);
	}
	
	protected POIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
