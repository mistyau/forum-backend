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
import org.springframework.stereotype.Service;

import com.example.demo.exception.PostCollectionException;
import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.model.Post;
import com.example.demo.model.Thread;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ThreadRepository;
import com.example.demo.repository.UserRepository;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ThreadRepository threadRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private ThreadService threadService;

	@Override
	public void createPost(String username, Post post) throws PostCollectionException {
		Optional<Thread> threadOptional = threadRepo.findById(post.getThreadId());
		if (!threadOptional.isPresent()) {
			throw new PostCollectionException(PostCollectionException.ThreadNotFound(post.getThreadId()));
		} else {
			User user = userRepo.findByUsername(username);
			post.setUserId(user.getId());
			post.setAuthor(username);
			post.setThreadId(post.getThreadId());
			post.setThreadSubject(threadOptional.get().getSubject());
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
	public List<Post> getAllUserPosts(String username) {
		List<Post> posts = postRepo.findByAuthor(username);
		if (posts.size() > 0) {
			return posts;
		} else {
			return new ArrayList<Post>();
		}
	}
	
	public List<Post> getUserPostsPage(String username, int page, int size, String sort) {
		Sort postsSort;
		if (sort.equals("old")) {
			postsSort = Sort.by("createdAt", "id").ascending();
		} else {
			postsSort = Sort.by("createdAt", "id").descending();
		}
		Pageable pageable = PageRequest.of(page, size, postsSort);
		List<Post> posts = postRepo.findByAuthorPage(username, pageable);
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
	public void updatePost(String username, String id, Post post) throws PostCollectionException {
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
	public void deletePostById(String username, String id) throws PostCollectionException, ThreadCollectionException {
		Optional<Post> postOptional = postRepo.findById(id);
		if (!postOptional.isPresent()) {
			throw new PostCollectionException(PostCollectionException.NotFoundException(id));
		} else {
			// update thread comments counter
			threadService.incComments(postOptional.get().getThreadId(), -1);
			postRepo.deleteById(id);		
		}
	}

}
