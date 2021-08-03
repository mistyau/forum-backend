package com.example.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public User registerNewUserAccount(User account) {
		User user = new User();
		user.setUsername(account.getUsername());
		user.setPassword(passwordEncoder.encode(account.getPassword()));
		user.setCreatedAt(new Date(System.currentTimeMillis()));
		return userRepo.save(user);
	}

}
