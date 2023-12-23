package com.venkat.airlinewebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venkat.airlinewebapp.entity.AppAuthority;


@Repository
public interface IAuthorityRepository extends JpaRepository<AppAuthority, Integer>{

	AppAuthority findByName(String name);
}
