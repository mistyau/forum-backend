package com.example.demo.service;

import java.util.List;

import com.example.demo.exception.LikedCollectionException;
import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.exception.UserCollectionException;
import com.example.demo.model.Liked;

public interface LikedService {
	
	public List<Liked> getUserLikes(String userId) throws UserCollectionException;
	
	public Liked getSingleLiked(String threadId, String username);
	
	public void createLike(String username, String threadId) throws ThreadCollectionException;
	
	public void deleteLike(String username, String threadId) throws LikedCollectionException, ThreadCollectionException;
}
