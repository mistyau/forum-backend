package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Liked;

@Repository
public interface LikedRepository extends MongoRepository<Liked, String> {
	
	List<Liked> findByUserId(String userId);

	@Query("{'threadId': ?0, 'userId': ?1}")
	Optional<Liked> findByThreadIdAndUserId(String threadId, String userId);
	
	@DeleteQuery("{'threadId': ?0}")
	void deleteByThreadId(final String threadId);
}
