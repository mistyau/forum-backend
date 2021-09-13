package com.example.demo.exception;

public class LikedCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LikedCollectionException(String message) {
		super(message);
	}
	
	public static String NotFoundException() {
		return "Like not found!";
	}
	
	public static String LikeAlreadyExists() {
		return "Like already exists";
	}
}
