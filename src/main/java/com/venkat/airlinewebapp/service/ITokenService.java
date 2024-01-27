package com.venkat.airlinewebapp.service;

import java.util.List;
import java.util.Optional;

import com.venkat.airlinewebapp.entity.AppToken;

public interface ITokenService {
	
	List<AppToken> findAllValidTokensByUser(Integer userId);
	
	Optional<AppToken> findByTokenValue(String token);
	
	void revokeAllUserTokens(Integer userId);
	
	void saveUserToken(AppToken appToken);
}
