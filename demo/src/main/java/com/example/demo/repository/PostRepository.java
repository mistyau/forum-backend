package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
	List<Post> findByThreadId(final String threadId);
	
	List<Post> findByAuthor(String author);
	
	@Query("{'author': ?0}")
	List<Post> findByAuthorPage(String author, Pageable pageable);
	
	@DeleteQuery("{'threadId': ?0}")
	void deleteByThreadId(final String threadId);
}
