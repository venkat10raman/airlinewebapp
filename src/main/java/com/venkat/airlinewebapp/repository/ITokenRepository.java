package com.venkat.airlinewebapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.venkat.airlinewebapp.entity.AppToken;
import com.venkat.airlinewebapp.entity.AppUser;

@Repository
public interface ITokenRepository extends JpaRepository<AppToken, Integer>{
	
//	@Query("select t from AppToken t inner join AppUser au on t.user.id = au.id where au.id = :userId ")
//	List<AppToken> findAllValidTokensByUser(Integer userId);
	
	List<AppToken> findByUser(AppUser user);
	
	Optional<AppToken> findByTokenValue(String token);
	

}
