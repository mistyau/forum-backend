package com.example.demo.exception;

public class ThreadCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ThreadCollectionException(String message) {
		super(message);
	}
	
	public static String NotFoundException(String id) {
		return "Thread with " + id + "not found!";
	}
	
	public static String ThreadAlreadyExists() {
		return "Thread with given name already exists";
	}
	
	public static String AuthorNotFound(String author) {
		return "Thread with " + author + "not found!";
	}
}
