package com.venkat.airlinewebapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.venkat.airlinewebapp.aop.LoggingInterceptor;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;


@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggingInterceptor());
	}
	
	@Bean
	public OpenAPI openApiConfig() {
		return new OpenAPI().info(
				new Info().title("Airline RESTFul Web Application")
				.version("1.0.0")
				.description("R for RESTFul Web Services")
				.termsOfService("http://venkat.com/terms")
				.license(new License().name("Venkat License").url("http://venkat.com")));
	}

}
