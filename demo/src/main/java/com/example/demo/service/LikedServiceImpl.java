package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Liked;
import com.example.demo.model.User;
import com.example.demo.repository.LikedRepository;
import com.example.demo.repository.UserRepository;

@Service
public class LikedServiceImpl implements LikedService {
	
	@Autowired
	LikedRepository likedRepo;
	
	@Autowired
	UserRepository userRepo;

	@Override
	public List<Liked> getUserLikes(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createLike(String username, String threadId) {
		// TODO: check if record already exists
		User user = userRepo.findByUsername(username);
		Liked like = new Liked();
		like.setUserId(user.getId());
		like.setThreadId(threadId);
		like.setCreatedAt(new Date(System.currentTimeMillis()));
		likedRepo.save(like);
	}

	@Override
	public Liked getSingleLiked(String threadId, String username) {
		User user = userRepo.findByUsername(username);
		Optional<Liked> likedOptional = likedRepo.findByThreadIdAndUserId(threadId, user.getId());	
		if (!likedOptional.isPresent()) {
			return null;
		} else {
			return likedOptional.get();
		}
	}

	@Override
	public void deleteLike(String username, String threadId) {
		// TODO Auto-generated method stub
		
	}

}
