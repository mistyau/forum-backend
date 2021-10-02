package com.example.demo.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="refreshTokens")
@CompoundIndex(def = "{'id': 1, 'jti': 1}")
public class RefreshToken {
	
	@Id
	private String id;
	
	@Indexed(unique=true)
	private String jti;
	
	@NotNull(message="User id cannot be null")
	private String userId;
	
	@Indexed(expireAfterSeconds=28800) // expires in 8 hours
	private Date createdAt;
	
	private Date updatedAt;
	
	private Boolean valid;
}
