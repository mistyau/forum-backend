package com.example.demo.service;

import java.util.List;

import com.example.demo.exception.PostCollectionException;
import com.example.demo.exception.ThreadCollectionException;
import com.example.demo.model.Post;

public interface PostService {
	
	public void createPost(String username, Post post) throws PostCollectionException;
	
	public List<Post> getAllPosts();
	
	public List<Post> getAllThreadPosts(String id);
	
	public List<Post> getAllUserPosts(String author);
	
	public List<Post> getUserPostsPage(String author, int page, int size, String sort);
	
	public Post getSinglePost(String id) throws PostCollectionException;
	
	public void updatePost(String username, String id, Post post) throws PostCollectionException;
	
	public void deletePostById(String username, String id) throws PostCollectionException, ThreadCollectionException;
}
