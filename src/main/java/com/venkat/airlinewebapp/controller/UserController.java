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

import com.venkat.airlinewebapp.dto.UserDto;
import com.venkat.airlinewebapp.security.AppUserPrinciple;
import com.venkat.airlinewebapp.security.util.JWTTokenUtil;
import com.venkat.airlinewebapp.service.IUserService;

@RestController
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
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
			return ResponseEntity.ok().headers(headers).body(user);
		} else { 
            throw new UsernameNotFoundException("invalid user request !"); 
        } 
		
	}

}
