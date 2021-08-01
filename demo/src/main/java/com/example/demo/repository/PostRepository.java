package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
	List<Post> findByThreadId(final String threadId);
	
	@DeleteQuery("{'threadId': ?0}")
	void deleteByThreadId(final String threadId);
}
