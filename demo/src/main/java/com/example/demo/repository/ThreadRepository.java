package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.MostCommonTagsAggregate;
import com.example.demo.model.Thread;
import com.example.demo.model.ThreadAggregate;

@Repository
public interface ThreadRepository extends MongoRepository<Thread, String> {
	
	@Query("{'subject': ?0}")
	Optional<Thread> findBySubject(String subject);
	
	List<Thread> findByAuthor(String author);
	
	@Query("{'tags': ?0 }")
	List<Thread> findByTag(String tag);
	
	@Aggregation(pipeline = {"{$lookup: {\n" + 
			"  from: 'likes',\n" + 
			"  let: {\n" + 
			"    thread_id: '$_id',\n" + 
			"    currentUser: {$toObjectId: ?0}\n" + 
			"  },\n" + 
			"  pipeline: [{\n" + 
			"    $match: {\n" + 
			"      $expr: {\n" + 
			"        $and: [\n" + 
			"          {$eq: [\"$$thread_id\", \"$threadId\"]},\n" + 
			"          {$eq: [\"$$currentUser\", \"$userId\"]}\n" + 
			"          ]\n" + 
			"      }\n" + 
			"    }\n" + 
			"  }],\n" + 
			"  as: 'matches'\n" + 
			"}}",
			"{$addFields: {\n" + 
			"  userLiked: { $in: [ {$toObjectId: ?0}, \"$matches.userId\"] }\n" + 
			"}}",
			"{$project: {\n" + 
			"  matches: 0\n" + 
			"}}"})
	List<ThreadAggregate> verySillyAggregation(String userid, Pageable page); 
	
	@Aggregation(pipeline = {"{$match: {\n" + 
			"  tags: { $exists: true }\n" + 
			"}}",
			"{$unwind: {\n" + 
			"  path: \"$tags\"\n" + 
			"}}",
			"{$group: {\n" + 
			"  _id: \"$tags\",\n" + 
			"  count: { $sum: 1 }\n" + 
			"}}",
			"{$sort: {\n" + 
			"  count: -1\n" + 
			"}}",
			"{$limit: 5}"})
	List<MostCommonTagsAggregate> commonTagsAggregation();
}
