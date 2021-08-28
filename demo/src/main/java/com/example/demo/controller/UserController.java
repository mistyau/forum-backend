package com.example.demo.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.UserCollectionException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping(path="/api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			User userToSave = userService.registerNewUserAccount(user);
			return new ResponseEntity<>(userToSave.getId(), HttpStatus.OK);
		} catch(ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (UserCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping(path = "/refresh/token", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public ResponseEntity<?> refreshToken(
			@RequestParam MultiValueMap<String, String> paramMap) {
		
		if (!(paramMap.getFirst("grant_type").equals("refresh_token"))) {
			return new ResponseEntity<>("Grant type must be refresh token", HttpStatus.BAD_REQUEST);
		}
		
		String refreshToken = paramMap.getFirst("refresh_token");
		
		if (refreshToken == null) {
			return new ResponseEntity<>("Refresh token missing", HttpStatus.BAD_REQUEST);
		}
	
		String user = Jwts.parserBuilder().require("role", "REFRESH_TOKEN")
						  .setSigningKey(System.getenv("SECRET_KEY").getBytes())
						  .build()
						  .parseClaimsJws(refreshToken)
						  .getBody()
						  .getSubject();
		
		if (user == null) {
			return new ResponseEntity<>("invalid_client", HttpStatus.BAD_REQUEST);
		}
		
		String accessToken = Jwts.builder()
						   		 .setSubject(user)
						   		 .setExpiration(new Date(System.currentTimeMillis() + 900_000))
						   		 .claim("role", "ACCESS_TOKEN")
						   		 .signWith(SignatureAlgorithm.HS512, System.getenv("SECRET_KEY").getBytes())    
						   		 .compact();
		String res = "{" + "\"" + "access_token" + "\"" + ":" + "\"" + accessToken + "\"" + ","
						+ "\"" + "token_type" + "\"" + ":" + "\"" + "Bearer" + "\"" + "}";

		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
