package com.example.demo.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="likes")
@CompoundIndex(def="{'threadId': 1, 'userId': 1}", unique=true)
public class Liked {
	
	@Id
	private String id;
	
	@NotNull(message="Thread id cannot be null")
	@Field(targetType = FieldType.OBJECT_ID)
	private String threadId;
	
	@NotNull(message="Thread id cannot be null")
	@Field(targetType = FieldType.OBJECT_ID)
	private String userId;
	
	private String threadAuthor;
	
	private String threadSubject;
	
	private Date createdAt;
}
