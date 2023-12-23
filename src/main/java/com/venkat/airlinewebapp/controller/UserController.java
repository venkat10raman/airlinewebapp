package com.venkat.airlinewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.venkat.airlinewebapp.dto.UserDto;
import com.venkat.airlinewebapp.service.IUserService;

@RestController
public class UserController {
	
	@Autowired
	private IUserService userService;
	
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

}
