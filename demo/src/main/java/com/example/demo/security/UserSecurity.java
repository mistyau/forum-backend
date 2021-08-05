package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
	
	public boolean hasUsername(Authentication auth, String username) {
		if (auth.getName().equals(username)) {
			return true;
		}
		
		return false;
	}
}
