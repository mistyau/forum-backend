package com.example.demo.security;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.model.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private static final long EXPIRATION_TIME = 900_000;
	private static final long REFRESH_EXPIRATION_TIME = 604_800_000;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		
		setFilterProcessesUrl("/api/v1/users/login");
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
				.claim("role", "ACCESS_TOKEN")
				.signWith(SignatureAlgorithm.HS512, System.getenv("SECRET_KEY").getBytes())    
				.compact();
	
		String refreshToken = Jwts.builder()
				.setSubject(((User) auth.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
				.claim("role", "REFRESH_TOKEN")
				.setId(UUID.randomUUID().toString())
				.signWith(SignatureAlgorithm.HS512, System.getenv("SECRET_KEY").getBytes())    
				.compact();
		
		//res.addHeader("Authorization", "Bearer " + token);
		
		//String body = ((User) auth.getPrincipal()).getUsername() + " " + token;
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		
		String body = "{\"" + "username" + "\":\"" + ((User) auth.getPrincipal()).getUsername() 
						+ "\"," + "\"" + "token" + "\":\"" + token + "\"," + "\n"
						+ "\"" + "refreshToken" + "\":\"" + refreshToken + "\","
						+ "\"" + "type" + "\":\"" + "Bearer"
						+ "\"}";
		
		res.getWriter().write(body);
		res.getWriter().flush();
	}
}
