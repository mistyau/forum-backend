package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.LikedCollectionException;
import com.example.demo.service.LikedService;

@RestController
@RequestMapping(path="/api/v1")
public class LikedController {

	@Autowired
	LikedService likedService;
	
	@DeleteMapping("/users/{username}/likes/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
		try {
			likedService.deleteLike(id);
			return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
		} catch (LikedCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
