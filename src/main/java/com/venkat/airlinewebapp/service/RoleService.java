package com.venkat.airlinewebapp.service;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venkat.airlinewebapp.dto.AuthorityDto;
import com.venkat.airlinewebapp.dto.RoleDto;
import com.venkat.airlinewebapp.entity.AppAuthority;
import com.venkat.airlinewebapp.entity.AppRole;
import com.venkat.airlinewebapp.repository.IAuthorityRepository;
import com.venkat.airlinewebapp.repository.IRoleRepository;


@Service
public class RoleService implements IRoleService {
	
	private static final Logger logger = LogManager.getLogger(RoleService.class);
	
	
	@Autowired
	private IRoleRepository roleRepo;
	
	@Autowired
	private IAuthorityRepository authorityRepo;

	@Override
	public RoleDto createRole(RoleDto roleDto) {
		
		AppRole  role = new AppRole();
		role.setName(roleDto.getName());
		role.setAuthorities(roleDto.getAuthorities().stream().map(a-> {
			AppAuthority authority = authorityRepo.findByName(a.getName());
			if(authority == null) {
				authority = authorityRepo.save(new AppAuthority(a.getName()));
			}
			return authority;
		}).collect(Collectors.toSet()));
		
		logger.info("RoleDto = "+roleDto);
		
		roleRepo.save(role);
		
		System.out.println("---------------------------");
		
		roleDto.setName(role.getName());
		
		roleDto.setAuthorities(role.getAuthorities().stream()
				.map(a -> new AuthorityDto(a.getName()))
				.collect(Collectors.toSet()));
		
		logger.info("RoleDto in response = "+ roleDto);
		
		return roleDto;
	}

}
