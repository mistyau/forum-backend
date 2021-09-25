package com.example.demo.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.model.MostCommonTagsAggregate;
import com.example.demo.model.Thread;
import com.example.demo.model.ThreadAggregate;

public interface ThreadService{
	
	public void createThread(String username, Thread thread) throws ThreadCollectionException;
	
	public List<Thread> getAllThreads();
	
	public Page<Thread> getAll(int page, int size, Sort sort);
	
	public List<ThreadAggregate> getThreadsAggregated(String username, int page, int size, Sort sort);
	
	public List<Thread> getAllUserThreads(String author) throws ThreadCollectionException;
	
	public List<Thread> getAllTagThreads(String thread);
	
	public List<MostCommonTagsAggregate> getMostCommonThreadTags();
	
	public Thread getSingleThread(String id) throws ThreadCollectionException;
	
	public void updateThread(String username, String id, Thread thread) throws ThreadCollectionException;
	
	public long incLikes(String id, int val) throws ThreadCollectionException;
	
	public long incComments(String id, int val) throws ThreadCollectionException;
	
	public void deleteThreadById(String username, String id) throws ThreadCollectionException;
}
