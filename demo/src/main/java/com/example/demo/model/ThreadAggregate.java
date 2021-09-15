package com.example.demo.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
public class ThreadAggregate {

	@Id
	private String id;
	
	@Field(targetType = FieldType.OBJECT_ID)
	@NotNull(message="User id cannot be null")
	private String userId;
	
	@NotNull(message="Username cannot be null")
	private String author; // don't really care about data redundancy in mongodb
	
	@NotBlank(message="Subject cannot be blank")
	private String subject;
	
	private String content;
	
	@Indexed
	private List<String> tags;

	private long likes;
	
	private boolean userLiked;
	
	@Indexed
	private Date createdAt;
	
	private Date updatedAt;
	
	private List<Post> posts;
}
