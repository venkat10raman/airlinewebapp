package com.venkat.airlinewebapp.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.venkat.airlinewebapp.dto.AuthorityDto;
import com.venkat.airlinewebapp.dto.RoleDto;
import com.venkat.airlinewebapp.dto.UserDto;
import com.venkat.airlinewebapp.entity.AppAuthority;
import com.venkat.airlinewebapp.entity.AppRole;
import com.venkat.airlinewebapp.entity.AppUser;
import com.venkat.airlinewebapp.repository.IRoleRepository;
import com.venkat.airlinewebapp.repository.IUserRepository;

@Service
public class UserService implements IUserService {
	
	private static final Logger logger = LogManager.getLogger(UserService.class);
	
	@Autowired
	private IUserRepository userRepo;
	
	@Autowired
	private IRoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDto createUser(UserDto userDto) {
		
		if(checkIfUserNameExists(userDto)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "User Name already Exists");
		}
		if(checkIfUserEmailIdExists(userDto)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "User Email Id  already Exists");
		}
		
		AppUser user = new AppUser();
		BeanUtils.copyProperties(userDto, user);
		user.setEmailId(user.getEmailId().toLowerCase());
		user.setUserName(user.getUserName().toLowerCase());
		
		Optional<AppRole> role = roleRepo.findById(1);
		Set<AppRole> roles  = new HashSet<>();
		roles.add(role.get());
		
		user.setRoles(roles);
		user.setEnabled(true);
		String password  = RandomStringUtils.random(7,true,true);
		logger.info("Password = "+password);
		
		user.setPassword(passwordEncoder.encode(password));
		
		user = userRepo.save(user);
		userDto = this.getUserDto(user);
		
		return userDto;
	}
	
	private UserDto getUserDto(AppUser user) {
		UserDto userDto = new UserDto();
		userDto.setEmailId(user.getEmailId());
		userDto.setEnabled(user.isEnabled());
		userDto.setFirstName(user.getFirstName());
		userDto.setId(user.getId());
		userDto.setLastName(user.getLastName());
		userDto.setUserName(user.getUserName());
		userDto.setPassword(user.getPassword());
		
		Set<RoleDto> rSet = new HashSet<>();
		for(AppRole r : user.getRoles()) {
			RoleDto rDto = new RoleDto();
			rDto.setName(r.getName());
			Set<AuthorityDto> aSet = new HashSet<>();
			
			for(AppAuthority a : r.getAuthorities()) {
				AuthorityDto aDto = new AuthorityDto(a.getName());
				aSet.add(aDto);
			}
			rDto.setAuthorities(aSet);
			rSet.add(rDto);
		}
		userDto.setRoles(rSet);
		
		return userDto;
	}

	
	public boolean checkIfUserNameExists(UserDto userDto) {
		return StringUtils.isNotBlank(userDto.getUserName()) && userRepo.findUserByUserName(userDto.getUserName().toLowerCase()) !=null;
	}
	private boolean checkIfUserEmailIdExists(UserDto userDto) {
		return StringUtils.isNotBlank(userDto.getEmailId()) && userRepo.findUserByEmailId(userDto.getEmailId().toLowerCase()) !=null;
	}

	@Override
	public UserDto updateUser(UserDto userDto) {
		
		AppUser user = userRepo.findUserByUserName(userDto.getUserName().toLowerCase());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		
		Set<AppRole> rSet = new HashSet<>();
		for(RoleDto rDto : userDto.getRoles()) {
			rSet.add(roleRepo.findByName(rDto.getName()));
		}
		user.setRoles(rSet);
		user = userRepo.save(user);
		userDto.setId(user.getId());
		
		return userDto;
	}
	
	@Override
	public UserDto findUserByUserName(String userName) {
		return this.getUserDto(this.userRepo.findUserByUserName(userName));
	}
	
}
