package com.venkat.airlinewebapp.security.util;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.venkat.airlinewebapp.security.AppUserPrinciple;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTTokenUtil {

	@Value("${jwt.secretKey}")
	private String secretKey;

	public String generateToken(AppUserPrinciple userPrinciple) {

		Map<String , Object> claims = new HashMap<>();

		this.setCustomClaims(claims , userPrinciple);
		
		return this.doGenerateToken(claims, userPrinciple.getUsername());
	}

	private void setCustomClaims(Map<String, Object> claims, AppUserPrinciple userPrinciple) {
		List<String> authorities = new ArrayList<>();
		for(GrantedAuthority auth : userPrinciple.getAuthorities()) {
			authorities.add(auth.getAuthority());
		}
		claims.put("authorities",authorities);

	}
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.addClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (5 * 60 * 60 * 1000)))
				.signWith(getSignKey())
				.compact();
	}
	
	private Key getSignKey() { 
		byte[] keyBytes= Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes); 
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Claims claims = this.getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);

	}

	public List<GrantedAuthority> getAuthoritiesClaimFromToken(String token){
		Claims claims = this.getAllClaimsFromToken(token);
		List<GrantedAuthority> returnValue = null;
		List<String>  authorities = (List) claims.get("authorities");

		if(authorities == null) return returnValue;

		returnValue = new ArrayList<>();

		return authorities.stream().map(SimpleGrantedAuthority :: new) .collect(Collectors.toList());

	}

	public String getUserNameFromToken(String token) {
		return this.getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return this.getClaimFromToken(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {

		Date expiration = this.getExpirationDateFromToken(token);

		return expiration.before(new Date());
	}

	public Boolean validateToken(String token, AppUserPrinciple userPrinciple) {
		String userName = this.getUserNameFromToken(token);
		return StringUtils.isNotBlank(userName) 
				&& userName.equals(userPrinciple.getUsername()) 
				&& !this.isTokenExpired(token);

	}
	
	public Boolean validateToken(String token, String principal) {
		String userName = this.getUserNameFromToken(token);
		return StringUtils.isNotBlank(userName) && userName.equals(principal) && !this.isTokenExpired(token);

	}

}