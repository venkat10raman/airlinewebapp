package com.venkat.airlinewebapp.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.venkat.airlinewebapp.exception.JWTAccessDeniedHandler;
import com.venkat.airlinewebapp.exception.JWTAuthenticationEntryPoint;
import com.venkat.airlinewebapp.security.JWTTokenAuthorizationFilter;
import com.venkat.airlinewebapp.service.LogoutService;
import com.venkat.airlinewebapp.service.UserPrincipleServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class AppSecurityConfig {

	private static final String[] PUBLIC_URLS = {
			"/login", "/error",
			"/airline/swagger-ui.html",
			"/airline/swagger-ui/**",
			"/v3/flight-api-docs",
			"/v3/flight-api-docs/**"
	};

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private UserPrincipleServiceImpl userPrincipleService;

	@Autowired
	private JWTTokenAuthorizationFilter jwtTokenAuthorizationFilter;
	
	@Autowired
	private JWTAccessDeniedHandler jwtAccessDeniedHandler;
	
	@Autowired
	private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private LogoutService logoutService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
		
		return http.csrf((csrf) -> csrf.disable())
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests((authorizeRequests) -> 
				authorizeRequests.requestMatchers(PUBLIC_URLS).permitAll()
				.anyRequest().authenticated())
			.sessionManagement((sm) -> 
				sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(authenticationProvider())
			.exceptionHandling((eh) ->
				eh.accessDeniedHandler(jwtAccessDeniedHandler)
					.authenticationEntryPoint(jwtAuthenticationEntryPoint)
 				)
			.logout((logout) -> 
					logout.logoutUrl("/logout")
					.addLogoutHandler(logoutService)
					.logoutSuccessHandler(
							(req, res, authentication) -> 
							SecurityContextHolder.clearContext()))
			.addFilterAfter(jwtTokenAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() { 
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
		authenticationProvider.setUserDetailsService(userPrincipleService); 
		authenticationProvider.setPasswordEncoder(passwordEncoder()); 
		return authenticationProvider; 
	}
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
        return config.getAuthenticationManager(); 
    } 
	
}
