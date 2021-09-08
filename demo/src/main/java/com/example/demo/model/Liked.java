package com.example.demo.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="likes")
@CompoundIndex(def="{'threadId': 1, 'userId': 1}, {'unique': true}")
public class Liked {
	
	@Id
	private String id;
	
	private String threadId;
	
	private String userId;
	
	private Date createdAt;
}
