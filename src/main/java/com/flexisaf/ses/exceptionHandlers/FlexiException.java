package com.flexisaf.ses.exceptionHandlers;
public class FlexiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2132835818008187546L;
	
	private String message;
	
	public FlexiException(String message) {
		super(message);
		this.message = message;
	}
	
	public String getErrorMessage() {
		return this.message;
	}
}