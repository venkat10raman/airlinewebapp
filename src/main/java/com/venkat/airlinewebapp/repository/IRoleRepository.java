package com.venkat.airlinewebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venkat.airlinewebapp.entity.AppRole;


@Repository
public interface IRoleRepository extends JpaRepository<AppRole, Integer>{

	AppRole findByName(String name);
	
}
