package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.model.Liked;
import com.example.demo.model.Thread;
import com.example.demo.service.LikedService;
import com.example.demo.service.ThreadService;

@RestController
@RequestMapping(path="/api/v1")
public class ThreadController {
	
	@Autowired
	private ThreadService threadService;
	
	@Autowired LikedService likedService;
	
	@GetMapping("/threads")
	public ResponseEntity<?> getAllThreads() {
		List<Thread> threads = threadService.getAllThreads();
		return new ResponseEntity<>(threads, threads.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(path="/threads", params = {"page", "size", "sort"})
	public ResponseEntity<?> getAll(@RequestParam("page") int page, 
			@RequestParam("size") int size, @RequestParam("sort") String sort) {
		Sort threadSort;
		if (sort.equals("new")) {
			threadSort = Sort.by("createdAt").descending();
		} else {
			threadSort = Sort.by("createdAt").ascending();
		}
		Page<Thread> threadPages = threadService.getAll(page, size, threadSort);
		List<Thread> threads = threadPages.getContent();
		Map<String, Object> response = new HashMap<>();
		response.put("threads", threads);
		response.put("currentPage", threadPages.getNumber());
		response.put("totalCount", threadPages.getTotalElements());
		response.put("totalPages", threadPages.getTotalPages());
		return new ResponseEntity<>(response, threads.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/users/{username}/threads")
	public ResponseEntity<?> createThread(@PathVariable("username") String username, @RequestBody Thread thread) {
		try {
			threadService.createThread(username, thread);
			return new ResponseEntity<Thread>(thread, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/users/{username}/threads")
	public ResponseEntity<?> getUserThreads(@PathVariable("username") String username) {
		try {
			return new ResponseEntity<>(threadService.getAllUserThreads(username), HttpStatus.OK);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/threads/search/{tag}")
	public ResponseEntity<?> getTagThreads(@PathVariable("tag") String tag) {
		List<Thread> threads = threadService.getAllTagThreads(tag);
		return new ResponseEntity<>(threads, threads.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/threads/{id}")
	public ResponseEntity<?> getSingleThread(@PathVariable("id") String id) {
		try {
			return new ResponseEntity<>(threadService.getSingleThread(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/users/{username}/threads/{id}")
	public ResponseEntity<?> updateById(@PathVariable("username") String username, @PathVariable("id") String id, @RequestBody Thread thread) {
		try {
			threadService.updateThread(username, id, thread);
			return new ResponseEntity<>("Updated thread with id " + id, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/users/{username}/threads/{id}/likes")
	public ResponseEntity<?> likeThread(@PathVariable("username") String username, @PathVariable("id") String id) {
		try {
			threadService.incLikes(id);
			likedService.createLike(username, id);
			return new ResponseEntity<>("Liked thread with id " + id, HttpStatus.OK);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/users/{username}/threads/{id}/likes")
	public ResponseEntity<?> userLikedThread(@PathVariable("username") String username, @PathVariable("id") String id) {
		Liked liked = likedService.getSingleLiked(id, username);
		if (liked == null) {
			return new ResponseEntity<>("No record of like", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(liked, HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{username}/threads/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("username") String username, @PathVariable("id") String id) {
		try {
			threadService.deleteThreadById(username, id);
			return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
