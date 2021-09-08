package com.example.demo.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
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
@Document(collection="threads")
public class Thread {
	
	@Id
	private String id;
	
	@NotNull(message="User id cannot be null")
	private String userId;
	
	@NotNull(message="Username cannot be null")
	private String author; // don't really care about data redundancy in mongodb
	
	@NotBlank(message="Subject cannot be blank")
	private String subject;
	
	private String content;
	
	private List<String> tags;
	
	private long likes;
	
	private Date createdAt;
	
	private Date updatedAt;
	
	private List<Post> posts;
}
