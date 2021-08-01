package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.PostCollectionException;
import com.example.demo.model.Post;
import com.example.demo.model.Thread;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ThreadRepository;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private ThreadRepository threadRepo;
	
	@Autowired
	private PostRepository postRepo;

	@Override
	public void createPost(String threadId, Post post) throws PostCollectionException {
		Optional<Thread> threadOptional = threadRepo.findById(threadId);
		if (!threadOptional.isPresent()) {
			throw new PostCollectionException(PostCollectionException.ThreadNotFound(threadId));
		} else {
			post.setThreadId(threadId);
			post.setCreatedAt(new Date(System.currentTimeMillis()));
			postRepo.save(post);
		}
	}

	@Override
	public List<Post> getAllPosts() {
		List<Post> posts = postRepo.findAll();
		if (posts.size() > 0) {
			return posts;
		} else {
			return new ArrayList<Post>();
		}
	}

	@Override
	public List<Post> getAllThreadPosts(String id) {
		List<Post> posts = postRepo.findByThreadId(id);
		
		if (posts.size() > 0) {
			return posts;
		} else {
			return new ArrayList<Post>();
		}
	}

	@Override
	public Post getSinglePost(String id) throws PostCollectionException {
		Optional<Post> optionalPost = postRepo.findById(id);
		if (!optionalPost.isPresent()) {
			throw new PostCollectionException(PostCollectionException.NotFoundException(id));
		} else {
			return optionalPost.get();
		}
	}

	@Override
	public void updatePost(String id, Post post) throws PostCollectionException {
		Optional<Post> postWithId = postRepo.findById(id);
		
		if (postWithId.isPresent()) {
			Post postToUpdate = postWithId.get();
			postToUpdate.setContent(post.getContent());
			postToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
			postRepo.save(postToUpdate);
		} else {
			throw new PostCollectionException(PostCollectionException.NotFoundException(id));
		}
	}

	@Override
	public void deletePostById(String id) throws PostCollectionException {
		Optional<Post> postOptional = postRepo.findById(id);
		if (!postOptional.isPresent()) {
			throw new PostCollectionException(PostCollectionException.NotFoundException(id));
		} else {
			postRepo.deleteById(id);
		}
	}

}
