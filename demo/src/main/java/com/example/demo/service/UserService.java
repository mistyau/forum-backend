package com.example.demo.service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.example.demo.exception.UserCollectionException;
import com.example.demo.model.User;
import com.example.demo.model.UserInput;

@Validated
public interface UserService {

	public User registerNewUserAccount(@Valid UserInput account) throws ConstraintViolationException, UserCollectionException;
}
