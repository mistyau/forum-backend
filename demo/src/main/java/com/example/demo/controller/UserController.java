package com.example.demo.controller;

import java.util.Date;
import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.RefreshTokenCollectionException;
import com.example.demo.exception.UserCollectionException;
import com.example.demo.model.User;
import com.example.demo.model.UserInput;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping(path="/api/v1/auth")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@RequestBody UserInput user) {
		try {
			User userToSave = userService.registerNewUserAccount(user);
			return new ResponseEntity<>(userToSave.getId(), HttpStatus.OK);
		} catch(ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (UserCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@SuppressWarnings("deprecation")
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
		
		// check if refresh token is valid. if not, invalidate all user tokens including current.
		// ..else return new access token and refresh token.
		
		String jtiToCheck = Jwts.parserBuilder().require("role", "REFRESH_TOKEN")
				  		.setSigningKey(System.getenv("SECRET_KEY").getBytes())
				  		.build()
				  		.parseClaimsJws(refreshToken)
				  		.getBody()
				  		.getId();
		
		if (jtiToCheck == null) {
			return new ResponseEntity<>("invalid_client", HttpStatus.BAD_REQUEST);
		}

		
		try {
			// invalidate all related tokens
			if (!refreshTokenService.isTokenValid(jtiToCheck)) { 
				refreshTokenService.invalidateUserRefreshTokens(user);
				return new ResponseEntity<>("invalid token", HttpStatus.BAD_REQUEST);
			}
			// invalidate current refresh token
			refreshTokenService.invalidateRefreshTokenById(jtiToCheck);
		} catch (RefreshTokenCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		// return new access token and refresh token
		String accessToken = Jwts.builder()
						   		 .setSubject(user)
						   		 .setExpiration(new Date(System.currentTimeMillis() + 900_000))
						   		 .claim("role", "ACCESS_TOKEN")
						   		 .signWith(SignatureAlgorithm.HS512, System.getenv("SECRET_KEY").getBytes())    
						   		 .compact();
		
		String jti = UUID.randomUUID().toString();
		
		String newRefreshToken = Jwts.builder()
				.setSubject(user)
				.setExpiration(new Date(System.currentTimeMillis() + 28_800_000))
				.claim("role", "REFRESH_TOKEN")
				.setId(jti)
				.signWith(SignatureAlgorithm.HS512, System.getenv("SECRET_KEY").getBytes())    
				.compact();
		
		refreshTokenService.saveRefreshToken(user, jti);
		
		String res = "{" + "\"" + "access_token" + "\"" + ":" + "\"" + accessToken + "\"" + ","
						+ "\"" + "refresh_token" + "\"" + ":" + "\"" + newRefreshToken + "\"" + ","
						+ "\"" + "token_type" + "\"" + ":" + "\"" + "Bearer" + "\"" + "}";

		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
