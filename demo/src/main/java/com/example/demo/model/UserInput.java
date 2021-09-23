package com.example.demo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {
	
	@Size(min=3, max=30, message="Username must be between 3 and 30 characters.")
	@Pattern(regexp="[a-zA-Z0-9]+", message="Only alphanumeric characters allowed.")
	private String username;
	
	@NotBlank(message = "Password cannot be blank")
	@Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters.")
	private String password;
}
