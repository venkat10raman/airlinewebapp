package com.venkat.airlinewebapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.venkat.airlinewebapp.entity.AppToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LogoutService implements LogoutHandler {

	private static final Logger logger = LogManager.getLogger(LogoutService.class);

	@Autowired
	private ITokenService tokenService;
	
	@Override
	public void logout(HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) {

		String requestToken = request.getHeader("Authorization");

		if(requestToken !=null && requestToken.startsWith("Bearer ")) {
			String jwtToken = requestToken.substring(7);
			AppToken appToken = tokenService.findByTokenValue(jwtToken).get();
			appToken.setExpired(true);
			tokenService.saveUserToken(appToken);
			logger.info("Logout is successful");
		} else {
			logger.warn("Logout is failed ");
		}
	}

}
