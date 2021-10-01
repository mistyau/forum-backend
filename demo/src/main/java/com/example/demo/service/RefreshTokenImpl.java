package com.example.demo.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.demo.exception.RefreshTokenCollectionException;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.User;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;

@Service
public class RefreshTokenImpl implements RefreshTokenService {

	@Autowired
	RefreshTokenRepository refreshTokenRepo;
	
	@Autowired UserRepository userRepo;
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public void saveRefreshToken(String username, String jti) {
		// check for duplicate jti
		
		RefreshToken token = new RefreshToken();
		User user = userRepo.findByUsername(username);
		token.setUserId(user.getId());
		token.setJti(jti);
		token.setValid(true);
		token.setCreatedAt(new Date(System.currentTimeMillis()));
		refreshTokenRepo.save(token);
	}

	@Override
	public void invalidateRefreshTokenById(String jti) throws RefreshTokenCollectionException {
		RefreshToken token = mongoTemplate.findOne(
				Query.query(Criteria.where("jti").is(jti)), RefreshToken.class);
		if (token == null) {
			throw new RefreshTokenCollectionException(RefreshTokenCollectionException.NotFoundException(jti));
		}
		token.setValid(false);
		token.setUpdatedAt(new Date(System.currentTimeMillis()));
		mongoTemplate.save(token, "refreshTokens");
	}

	@Override
	public void invalidateUserRefreshTokens(String username) {
		User user = userRepo.findByUsername(username);
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(user.getId()));
		Update update = new Update();
		update.set("valid", false);
		update.set("updatedAt", new Date(System.currentTimeMillis()));
		mongoTemplate.updateMulti(query, update, RefreshToken.class);
	}

	@Override
	public boolean isTokenValid(String jti) throws RefreshTokenCollectionException {
		Optional<RefreshToken> token = refreshTokenRepo.findByJti(jti);
		if (!token.isPresent()) {
			throw new RefreshTokenCollectionException(RefreshTokenCollectionException.NotFoundException(jti));
		}
		
		RefreshToken otherToken = token.get();
		if (otherToken.getValid()) {
			return true;
		}
		
		return false;
	}

}
