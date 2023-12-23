package com.venkat.airlinewebapp.security;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.venkat.airlinewebapp.security.util.JWTTokenUtil;
import com.venkat.airlinewebapp.service.UserPrincipleServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTTokenAuthorizationFilter extends OncePerRequestFilter {

	private static final Logger logger = LogManager.getLogger(JWTTokenAuthorizationFilter.class);

	@Autowired
	private JWTTokenUtil jwtTokenUtil;

	@Autowired
	private UserPrincipleServiceImpl userPrincipleService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		if(request.getMethod().equalsIgnoreCase("OPTIONS")) {
			response.setStatus(HttpStatus.OK.value());
		} else {

			String requestToken = request.getHeader("Authorization"); //"Bearer asdf3436sdfgdg2564356...."

			String userName = null;
			String jwtToken = null;

			if(requestToken !=null && requestToken.startsWith("Bearer ")) {
				jwtToken = requestToken.substring(7);
				userName = this.jwtTokenUtil.getUserNameFromToken(jwtToken);
			} else {
				logger.warn("JWT token is null or does not begin with Bearer String for url "+ request.getRequestURI());
			}

			if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails =  this.userPrincipleService.loadUserByUsername(userName);
				
				if(userDetails !=null && this.jwtTokenUtil.validateToken(jwtToken, userDetails.getUsername())) {

					List<GrantedAuthority> authorities = this.jwtTokenUtil.getAuthoritiesClaimFromToken(jwtToken);

					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, authorities);
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);

				} else {
					SecurityContextHolder.clearContext();
				}
			}

		}
		filterChain.doFilter(request, response);
	}
	
	public Authentication getAthentication(String userName, List<GrantedAuthority> authorities, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, authorities);
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return authenticationToken;
	}

}
