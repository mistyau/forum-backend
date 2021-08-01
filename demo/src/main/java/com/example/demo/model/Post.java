package com.example.demo.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
	
	@NotNull(message = "thread id cannot be null")
	private String threadId;
	
	@NotNull(message = "content cannot be null")
	private String content;
	
	private Date createdAt;
	
	private Date updatedAt;
}
