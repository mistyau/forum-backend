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
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.model.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private static final long EXPIRATION_TIME = 900_000;
	private static final long REFRESH_EXPIRATION_TIME = 604_800_000;
	@Autowired RefreshTokenService refreshTokenService;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
		this.authenticationManager = authenticationManager;
		this.refreshTokenService = ctx.getBean(RefreshTokenService.class);
		
		setFilterProcessesUrl("/api/v1/auth/login");
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
	
		String jti = UUID.randomUUID().toString();
		String refreshToken = Jwts.builder()
				.setSubject(((User) auth.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
				.claim("role", "REFRESH_TOKEN")
				.setId(jti)
				.signWith(SignatureAlgorithm.HS512, System.getenv("SECRET_KEY").getBytes())    
				.compact();
		
		refreshTokenService.saveRefreshToken(((User) auth.getPrincipal()).getUsername(), jti);
		
		//res.addHeader("Authorization", "Bearer " + token);
		
		//String body = ((User) auth.getPrincipal()).getUsername() + " " + token;
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		
		String body = "{\"" + "username" + "\":\"" + ((User) auth.getPrincipal()).getUsername() 
						+ "\"," + "\"" + "access_token" + "\":\"" + token + "\"," + "\n"
						+ "\"" + "refresh_token" + "\":\"" + refreshToken + "\","
						+ "\"" + "token_type" + "\":\"" + "Bearer" + "\","
						+ "\"" + "expires_in" + "\":" + "\"" + "900"
						+ "\"}";
		
		res.getWriter().write(body);
		res.getWriter().flush();
	}
}
