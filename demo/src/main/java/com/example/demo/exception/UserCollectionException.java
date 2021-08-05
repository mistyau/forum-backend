package com.example.demo.exception;

public class UserCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserCollectionException(String message) {
		super(message);
	}
	
	public static String UsernameAlreadyExists() {
		return "Username already exists!";
	}
}
