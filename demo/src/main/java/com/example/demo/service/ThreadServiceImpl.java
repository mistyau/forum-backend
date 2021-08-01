package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.model.Thread;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ThreadRepository;

@Service
public class ThreadServiceImpl implements ThreadService {

	@Autowired
	private ThreadRepository threadRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	@Override
	public void createThread(Thread thread) throws ThreadCollectionException {
		Optional<Thread> threadOptional = threadRepo.findBySubject(thread.getSubject());
		if (threadOptional.isPresent()) {
			throw new ThreadCollectionException(ThreadCollectionException.ThreadAlreadyExists());
		} else {
			thread.setCreated(new Date(System.currentTimeMillis()));
			threadRepo.save(thread);
		}
		
	}

	@Override
	public List<Thread> getAllThreads() {
		List<Thread> threads = threadRepo.findAll();
		if (threads.size() > 0) {
			return threads;
		} else {
			return new ArrayList<Thread>();
		}
	}

	@Override
	public Thread getSingleThread(String id) throws ThreadCollectionException {
		Optional<Thread> optionalThread = threadRepo.findById(id);
		if (!optionalThread.isPresent()) {
			throw new ThreadCollectionException(ThreadCollectionException.NotFoundException(id));	
		} else {
			return optionalThread.get();
		}
	}

	@Override
	public void updateThread(String id, Thread thread) throws ThreadCollectionException {
		Optional<Thread> threadWithId = threadRepo.findById(id);
		Optional<Thread> threadWithSameName = threadRepo.findBySubject(thread.getSubject());
		
		if (threadWithId.isPresent()) {
			
			if (threadWithSameName.isPresent() && !threadWithSameName.get().getId().equals(id)) {
				throw new ThreadCollectionException(ThreadCollectionException.ThreadAlreadyExists());
			}
			
			Thread threadToUpdate = threadWithId.get();
			threadToUpdate.setSubject(thread.getSubject());
			threadToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
			threadRepo.save(threadToUpdate);
		} else {
			throw new ThreadCollectionException(ThreadCollectionException.NotFoundException(id));
		}
	}

	@Override
	public void deleteThreadById(String id) throws ThreadCollectionException {
		Optional<Thread> threadOptional = threadRepo.findById(id);
		if (!threadOptional.isPresent()) {
			throw new ThreadCollectionException(ThreadCollectionException.NotFoundException(id));
		} else {
			threadRepo.deleteById(id);
			postRepo.deleteByThreadId(id);
		}
	}
	
}
