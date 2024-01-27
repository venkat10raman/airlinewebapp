package com.venkat.airlinewebapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venkat.airlinewebapp.entity.AppToken;
import com.venkat.airlinewebapp.entity.AppUser;
import com.venkat.airlinewebapp.repository.ITokenRepository;

@Service
public class TokenServiceImpl implements ITokenService {

	@Autowired
	private ITokenRepository tokenRepository;


	@Override
	public List<AppToken> findAllValidTokensByUser(Integer userId) {
		AppUser user = new AppUser();
		user.setId(userId);
		List<AppToken> list = tokenRepository.findByUser(user)
		.stream()
		.filter(t -> !t.expired || !t.revoked)
		.collect(Collectors.toList());
		return list;
	}

	@Override
	public Optional<AppToken> findByTokenValue(String token) {
		return tokenRepository.findByTokenValue(token);
	}

	@Override
	public void revokeAllUserTokens(Integer userId) {
		List<AppToken> list = findAllValidTokensByUser(userId);
		if(list.isEmpty()) {
			return;
		}
		list.forEach(t -> {
			t.setExpired(true);
			t.setRevoked(true);
		});
		tokenRepository.saveAll(list);
	}

	@Override
	public void saveUserToken(AppToken appToken) {
		tokenRepository.save(appToken);
	}

}
