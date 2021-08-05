package com.example.demo.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.model.Thread;

public interface ThreadService{
	
	public void createThread(String username, Thread thread) throws ThreadCollectionException;
	
	public List<Thread> getAllThreads();
	
	public Thread getSingleThread(String id) throws ThreadCollectionException;
	
	public void updateThread(String username, String id, Thread thread) throws ThreadCollectionException;
	
	public void deleteThreadById(String username, String id) throws ThreadCollectionException;
}
