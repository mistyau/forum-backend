package com.example.demo.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
@Document(collection="threads")
public class Thread {
	
	@Id
	private String id;
	
	@Field(targetType = FieldType.OBJECT_ID)
	@NotNull(message="User id cannot be null")
	private String userId;
	
	@NotNull(message="Username cannot be null")
	private String author; // don't really care about data redundancy in mongodb
	
	@Size(min = 1, max = 80, message = "Subject must be 80 characters or less.")
	@NotBlank(message="Subject cannot be blank.")
	private String subject;
	
	@Size(max = 500, message = "Content must be 500 characters or less.")
	private String content;
	
	@Size(max = 10, message = "Maximum of 10 tags allowed.")
	@Indexed
	private List<@NotBlank(message = "Tag cannot be blank.") @Size(max = 30, message = "Length of tag must be 30 characters or less.")String> tags;

	private long likes;
	
	private long comments;
	
	@Indexed
	private Date createdAt;
	
	private Date updatedAt;
	
	private List<Post> posts;
}
