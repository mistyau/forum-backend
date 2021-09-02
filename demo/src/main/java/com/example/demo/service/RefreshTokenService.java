package com.example.demo.service;

import com.example.demo.exception.RefreshTokenCollectionException;

public interface RefreshTokenService {
	
	public void saveRefreshToken(String username, String jti);
	
	public boolean isTokenValid(String jti) throws RefreshTokenCollectionException;
	
	public void invalidateRefreshTokenById(String id);
	
	public void invalidateUserRefreshTokens(String userId);
}
