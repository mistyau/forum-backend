package com.example.demo.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
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
@Document(collection="posts")
public class Post {
	
	@Id
	private String id;
	
	@Field(targetType = FieldType.OBJECT_ID)
	@NotNull(message="User id cannot be null")
	private String userId;
	
	@NotNull(message="Username cannot be null")
	private String author;
	
	@Field(targetType = FieldType.OBJECT_ID)
	@NotNull(message = "Thread id cannot be null")
	private String threadId;
	
	private String threadSubject;
	
	@NotNull(message = "Content cannot be null")
	private String content;
	
	private Date createdAt;
	
	private Date updatedAt;
}
