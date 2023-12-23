package com.venkat.airlinewebapp.service;


import com.venkat.airlinewebapp.dto.UserDto;


public interface IUserService {

	public UserDto createUser(UserDto userDto);
	public UserDto updateUser(UserDto userDto);
	public UserDto findUserByUserName(String userName);
	
}
