package com.venkat.airlinewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.venkat.airlinewebapp.dto.AuthenticationRequest;
import com.venkat.airlinewebapp.dto.AuthenticationResponse;
import com.venkat.airlinewebapp.dto.UserDto;
import com.venkat.airlinewebapp.entity.AppToken;
import com.venkat.airlinewebapp.entity.AppUser;
import com.venkat.airlinewebapp.security.AppUserPrinciple;
import com.venkat.airlinewebapp.security.util.JWTTokenUtil;
import com.venkat.airlinewebapp.service.ITokenService;
import com.venkat.airlinewebapp.service.IUserService;

@RestController
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
	@Autowired
	private ITokenService tokenService;

	@PostMapping(path = {"/register-user"}, 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
		userDto = userService.createUser(userDto);
		return ResponseEntity.ok(userDto);
	}

	@PutMapping(path = {"/update-user"}, 
			consumes = {MediaType.APPLICATION_JSON_VALUE}, 
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
		userDto = userService.updateUser(userDto);
		return ResponseEntity.ok(userDto);
	}

	@PostMapping(path="/login",
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword())); 

		if (authentication.isAuthenticated()) {
			UserDto user = this.userService.findUserByUserName(userDto.getUserName());
			user.setPassword(null);
			String jwtToken = this.jwtTokenUtil.generateToken(new AppUserPrinciple(user));
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer "+jwtToken);
			
			saveToken(user, jwtToken);

			return ResponseEntity.ok().headers(headers).body(user);
		} else { 
			throw new UsernameNotFoundException("invalid user request !"); 
		} 
	}

	@PostMapping(path = "/authenticate",
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<AuthenticationResponse> authenticate(
			@RequestBody AuthenticationRequest request
			) {
		
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())); 

		if (authentication.isAuthenticated()) {
			UserDto user = this.userService.findUserByUserName(request.getUserName());
			String jwtToken = this.jwtTokenUtil.generateToken(new AppUserPrinciple(user));
			
			saveToken(user, jwtToken);
			AuthenticationResponse response = new AuthenticationResponse();
			response.setAccessToken(jwtToken);

			return ResponseEntity.ok().body(response);
		} else { 
			throw new UsernameNotFoundException("invalid user request !"); 
		} 
	}
	
	private void saveToken(UserDto user, String token) {
		AppUser appUser = new AppUser();
		appUser.setId(user.getId());
		AppToken ap = new AppToken();
		ap.setTokenValue(token);
		ap.setUser(appUser);
		ap.setExpired(false);
		ap.setRevoked(false);
		tokenService.saveUserToken(ap);
	}
	
}
