package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Thread;

@Repository
public interface ThreadRepository extends MongoRepository<Thread, String> {
	
	@Query("{'subject': ?0}")
	Optional<Thread> findBySubject(String subject);
}
