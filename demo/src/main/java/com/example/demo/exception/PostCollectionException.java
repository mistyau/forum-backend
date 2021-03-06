package com.example.demo.exception;

public class PostCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PostCollectionException(String message) {
		super(message);
	}

	public static String NotFoundException(String id) {
		return "Post with id " + id + " not found!";
	}
	
	public static String ThreadNotFound(String id) {
		return "Thread with id " + id + " not found!";
	}
}
