package com.example.demo.service;

import com.example.demo.exception.UserCollectionException;
import com.example.demo.model.User;

public interface UserService {

	public User registerNewUserAccount(User account) throws UserCollectionException;
}
