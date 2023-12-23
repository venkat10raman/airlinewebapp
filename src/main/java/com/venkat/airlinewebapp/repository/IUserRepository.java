package com.venkat.airlinewebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venkat.airlinewebapp.entity.AppUser;


@Repository
public interface IUserRepository extends JpaRepository<AppUser, Integer>{
	
	AppUser findUserByUserName(String userName);
	
	AppUser findUserByEmailId(String emailId);

}
