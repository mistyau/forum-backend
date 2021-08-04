package com.example.demo.security;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private static final long EXPIRATION_TIME = 900_000;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		
		setFilterProcessesUrl("/api/users/login");
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		try {
			com.example.demo.model.User creds = new ObjectMapper().readValue(req.getInputStream(), com.example.demo.model.User.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getUsername(), 
							creds.getPassword(), 
							new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException {
		String token = Jwts.builder()
				.setSubject(((User) auth.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, System.getenv("SECRET_KEY").getBytes())    
				.compact();
		res.addHeader("Authorization", "Bearer " + token);
	}
}
