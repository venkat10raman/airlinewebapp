package com.venkat.airlinewebapp.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.venkat.airlinewebapp.dto.AuthorityDto;
import com.venkat.airlinewebapp.dto.RoleDto;
import com.venkat.airlinewebapp.dto.UserDto;
import com.venkat.airlinewebapp.entity.AppAuthority;
import com.venkat.airlinewebapp.entity.AppRole;
import com.venkat.airlinewebapp.entity.AppUser;
import com.venkat.airlinewebapp.repository.IUserRepository;
import com.venkat.airlinewebapp.security.AppUserPrinciple;

@Service
public class UserPrincipleServiceImpl implements UserDetailsService {
	
	private static final Logger logger = LogManager.getLogger(UserPrincipleServiceImpl.class);

	@Autowired
	private IUserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		AppUser user = userRepo.findUserByUserName(userName);

		if(user == null) {
			logger.warn("User Not Found {}" , userName);
			throw new UsernameNotFoundException("User Not Found "+ userName);
		}
		UserDto dto = this.getUserDto(user);

		AppUserPrinciple principle = new AppUserPrinciple(dto);

		return principle;
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


}
