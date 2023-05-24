package com.naruku.exception;
/**
 * 异常类
 * @author herche
 * @date 2022/10/01
 */
public class IORuntimeException extends RuntimeException {
	
	public IORuntimeException() {
		super();
	}
	
	public IORuntimeException(String message) {
		super(message);
	}
	
	public IORuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IORuntimeException(Throwable cause) {
		super(cause);
	}
	
	protected IORuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	/**
	 * 导致这个异常的异常是否是指定类型的异常
	 *
	 * @param clazz 异常类
	 * @return 是否为指定类型异常
	 */
	public boolean causeInstanceOf(Class<? extends Throwable> clazz) {
		final Throwable cause = this.getCause();
		return null != clazz && clazz.isInstance(cause);
	}
	
}