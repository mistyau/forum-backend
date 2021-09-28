package com.example.demo.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.PostCollectionException;
import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
import com.example.demo.service.ThreadService;

@RestController
@RequestMapping("/api/v1")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private ThreadService threadService;
	
	@GetMapping("/posts")
	public ResponseEntity<?> getAllPosts() {
		List<Post> posts = postService.getAllPosts();
		return new ResponseEntity<>(posts, posts.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/threads/{id}/posts")
	public ResponseEntity<?> getAllThreadPosts(@PathVariable("id") String id) {
		List<Post> posts = postService.getAllThreadPosts(id);
		return new ResponseEntity<>(posts, posts.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/users/{username}/posts")
	public ResponseEntity<?> createPost(@PathVariable("username") String username, @RequestBody Post post) {
		try {
			postService.createPost(username, post);
			// update thread comments count
			threadService.incComments(post.getThreadId(), 1);
			return new ResponseEntity<Post>(post, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (PostCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/threads/{thread_id}/posts/{id}")
	public ResponseEntity<?> getSinglePost(@PathVariable("id") String id) {
		try {
			return new ResponseEntity<>(postService.getSinglePost(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/users/{username}/posts")
	public ResponseEntity<?> getUserPosts(@PathVariable("username") String username) {
		try {
			return new ResponseEntity<>(postService.getAllUserPosts(username), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/users/{username}/posts/{id}")
	public ResponseEntity<?> updateById(@PathVariable("username") String username, @PathVariable("id") String id, @RequestBody Post post) {
		try {
			postService.updatePost(username, id, post);
			return new ResponseEntity<>("Updated post with id " + id, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (PostCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/users/{username}/posts/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("username") String username, @PathVariable("id") String id) {
		try {
			postService.deletePostById(username, id);
			return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
		} catch (PostCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
