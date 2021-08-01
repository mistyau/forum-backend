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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.PostCollectionException;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;

@RestController
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@GetMapping("/posts")
	public ResponseEntity<?> getAllPosts() {
		List<Post> posts = postService.getAllPosts();
		return new ResponseEntity<>(posts, posts.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("thread/{id}/posts")
	public ResponseEntity<?> getAllThreadPosts(@PathVariable("id") String id) {
		List<Post> posts = postService.getAllThreadPosts(id);
		return new ResponseEntity<>(posts, posts.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/thread/{id}/post")
	public ResponseEntity<?> createPost(@PathVariable("id") String id, @RequestBody Post post) {
		try {
			postService.createPost(id, post);
			return new ResponseEntity<Post>(post, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (PostCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/post/{id}")
	public ResponseEntity<?> getSinglePost(@PathVariable("id") String id) {
		try {
			return new ResponseEntity<>(postService.getSinglePost(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/post/{id}")
	public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody Post post) {
		try {
			postService.updatePost(id, post);
			return new ResponseEntity<>("Updated thread witht id " + id, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (PostCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/post/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
		try {
			postService.deletePostById(id);
			return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
		} catch (PostCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
