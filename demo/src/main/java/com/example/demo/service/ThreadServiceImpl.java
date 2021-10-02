package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.model.MostCommonTagsAggregate;
import com.example.demo.model.Thread;
import com.example.demo.model.ThreadAggregate;
import com.example.demo.model.User;
import com.example.demo.repository.LikedRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ThreadRepository;
import com.example.demo.repository.UserRepository;
import com.mongodb.client.result.UpdateResult;

@Service
public class ThreadServiceImpl implements ThreadService {
	
	@Autowired UserRepository userRepo;

	@Autowired
	private ThreadRepository threadRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private LikedRepository likedRepo;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void createThread(String username, Thread thread) throws ThreadCollectionException {
		User user = userRepo.findByUsername(username);
		thread.setUserId(user.getId());
		thread.setAuthor(username);
		thread.setLikes(0);
		thread.setCreatedAt(new Date(System.currentTimeMillis()));
		threadRepo.save(thread);
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
	public List<Thread> getAllUserThreads(String author) throws ThreadCollectionException {
		List<Thread> threads = threadRepo.findByAuthor(author);
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
	public void updateThread(String username, String id, Thread thread) throws ThreadCollectionException {
		Optional<Thread> threadWithId = threadRepo.findById(id);
		
		if (threadWithId.isPresent()) {
			Thread threadToUpdate = threadWithId.get();
			//threadToUpdate.setSubject(thread.getSubject());
			threadToUpdate.setContent(thread.getContent());
			threadToUpdate.setTags(thread.getTags());
			threadToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
			threadRepo.save(threadToUpdate);
		} else {
			throw new ThreadCollectionException(ThreadCollectionException.NotFoundException(id));
		}
	}

	@Override
	public void deleteThreadById(String username, String id) throws ThreadCollectionException {
		Optional<Thread> threadOptional = threadRepo.findById(id);
		if (!threadOptional.isPresent()) {
			throw new ThreadCollectionException(ThreadCollectionException.NotFoundException(id));
		} else {
			threadRepo.deleteById(id);
			postRepo.deleteByThreadId(id);
			likedRepo.deleteByThreadId(id);
		}
	}

	@Override
	public List<Thread> getAllTagThreads(String tag) {
		List<Thread> threads = threadRepo.findByTag(tag);
		if (threads.size() > 0) {
			return threads;
		} else {
			return new ArrayList<Thread>();
		}
	}

	@Override
	public Page<Thread> getAll(int page, int size, Sort sort) {
		Pageable pageable = PageRequest.of(page, size, sort);
		return threadRepo.findAll(pageable);
	}

	@Override
	public long incLikes(String id, int val) throws ThreadCollectionException {
		Query query = new Query(Criteria.where("id").is(id));
		Thread thread = mongoTemplate.findOne(query, Thread.class);
		if (thread == null) {
			throw new ThreadCollectionException(ThreadCollectionException.NotFoundException(id));
		}
		Update update = new Update().inc("likes", val);
		UpdateResult ur = mongoTemplate.updateFirst(query, update, Thread.class);
		return ur.getModifiedCount();
	}

	@Override
	public List<ThreadAggregate> getThreadsAggregated(String username, int page, int size, Sort sort) {
		User user = userRepo.findByUsername(username);
		Pageable pageable = PageRequest.of(page, size, sort);
		List<ThreadAggregate> threads = threadRepo.verySillyAggregation(user == null ? null : user.getId(), pageable);
		return threads;
	}

	@Override
	public long incComments(String id, int val) throws ThreadCollectionException {
		Query query = new Query(Criteria.where("id").is(id));
		Thread thread = mongoTemplate.findOne(query, Thread.class);
		if (thread == null) {
			throw new ThreadCollectionException(ThreadCollectionException.NotFoundException(id));
		}
		Update update = new Update().inc("comments", val);
		UpdateResult ur = mongoTemplate.updateFirst(query, update, Thread.class);
		return ur.getModifiedCount();
	}

	@Override
	public List<MostCommonTagsAggregate> getMostCommonThreadTags() {
		List<MostCommonTagsAggregate> tags = threadRepo.commonTagsAggregation();
		if (tags.size() > 0) {
			return tags;
		} else {
			return new ArrayList<MostCommonTagsAggregate>();
		}
	}
	
}
