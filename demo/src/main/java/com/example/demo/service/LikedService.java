package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Liked;

public interface LikedService {
	
	public List<Liked> getUserLikes(String userId);
	
	public Liked getSingleLiked(String threadId, String username);
	
	public void createLike(String username, String threadId);
	
	public void deleteLike(String username, String threadId);
}
