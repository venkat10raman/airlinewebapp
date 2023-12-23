package com.venkat.airlinewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.venkat.airlinewebapp.dto.RoleDto;
import com.venkat.airlinewebapp.service.IRoleService;

@RestController
public class RoleController {

	@Autowired
	private IRoleService roleService;

	@PostMapping(path = {"/create-role"}, 
			consumes = {MediaType.APPLICATION_JSON_VALUE}, 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
		roleDto = roleService.createRole(roleDto);
		return ResponseEntity.ok(roleDto);
	}
}
