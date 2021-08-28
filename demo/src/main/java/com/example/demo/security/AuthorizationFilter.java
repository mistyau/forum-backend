package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req,
									HttpServletResponse res,
									FilterChain chain) throws IOException, ServletException {
		String header = req.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer")) {
			chain.doFilter(req, res);
			return;
		}
		
		UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(req, res);
	}

	@SuppressWarnings("deprecation")
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		if (token != null) {
			String user = Jwts.parser().setSigningKey(System.getenv("SECRET_KEY").getBytes())
											.parseClaimsJws(token.replace("Bearer", ""))
											.getBody()
											.getSubject();
			if (user != null) {
				try {
					Jwts.parserBuilder().require("role", "ACCESS_TOKEN")
						.setSigningKey(System.getenv("SECRET_KEY").getBytes())
						.build()
						.parseClaimsJws(token.replace("Bearer", ""));
				} catch(MissingClaimException ice) {
					
				}
				
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		
		return null;
	}
}
