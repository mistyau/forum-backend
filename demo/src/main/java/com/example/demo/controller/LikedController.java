package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.LikedCollectionException;
import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.exception.UserCollectionException;
import com.example.demo.service.LikedService;
import com.example.demo.service.ThreadService;

@RestController
@RequestMapping(path="/api/v1")
public class LikedController {

	@Autowired
	private LikedService likedService;
	
	@Autowired
	private ThreadService threadService;
	
	@GetMapping("/users/{username}/liked")
	public ResponseEntity<?> getAllUserLikes(@PathVariable("username") String username) {
		try {
			return new ResponseEntity<>(likedService.getUserLikes(username), HttpStatus.OK);
		} catch (UserCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/users/{username}/liked/{thread}")
	public ResponseEntity<?> deleteById(@PathVariable("username") String username, @PathVariable("thread") String threadId) {
		try {
			likedService.deleteLike(username, threadId);
			threadService.incLikes(threadId, -1);
			return new ResponseEntity<>("Successfully deleted like with thread id " + threadId, HttpStatus.OK);
		} catch (LikedCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
