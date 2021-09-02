package com.example.demo.exception;

public class RefreshTokenCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RefreshTokenCollectionException(String message) {
		super(message);
	}

	public static String NotFoundException(String jti) {
		return "Refresh token with jti " + jti + " not found!";
	}
}
