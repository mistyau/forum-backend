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

import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.model.Thread;
import com.example.demo.service.ThreadService;

@RestController
public class ThreadController {
	
	@Autowired
	private ThreadService threadService;
	
	@GetMapping("/threads")
	public ResponseEntity<?> getAllThreads() {
		List<Thread> threads = threadService.getAllThreads();
		return new ResponseEntity<>(threads, threads.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/thread")
	public ResponseEntity<?> createThread(@RequestBody Thread thread) {
		try {
			threadService.createThread(thread);
			return new ResponseEntity<Thread>(thread, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/thread/{id}")
	public ResponseEntity<?> getSingleThread(@PathVariable("id") String id) {
		try {
			return new ResponseEntity<>(threadService.getSingleThread(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/thread/{id}")
	public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody Thread thread) {
		try {
			threadService.updateThread(id, thread);
			return new ResponseEntity<>("Updated thread with id " + id, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/thread/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
		try {
			threadService.deleteThreadById(id);
			return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
		} catch (ThreadCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
