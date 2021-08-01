package com.example.demo.model;

import java.util.Date;
import java.util.List;

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
	
	@NotNull(message="Subject cannot be null")
	private String subject;
	
	private Date created;
	
	private Date updatedAt;
	
	private List<Post> posts;
}
