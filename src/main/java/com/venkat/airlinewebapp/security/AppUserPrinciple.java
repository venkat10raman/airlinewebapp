package com.venkat.airlinewebapp.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.venkat.airlinewebapp.dto.RoleDto;
import com.venkat.airlinewebapp.dto.UserDto;


public class AppUserPrinciple implements UserDetails {

	private static final long serialVersionUID = 1357032044060256841L;
	
	private UserDto userDto;
	
	public AppUserPrinciple(UserDto userDto) {
		super();
		this.userDto = userDto;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<RoleDto> roleDtoList = this.userDto.getRoles();
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		for(RoleDto roleDto : roleDtoList) {
			authorities.addAll(
					roleDto.getAuthorities().stream()
					.map(a -> new SimpleGrantedAuthority(a.getName()))
					.collect(Collectors.toList()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.userDto.getPassword();
	}

	@Override
	public String getUsername() {
		return this.userDto.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.userDto.isEnabled();
	}

}
