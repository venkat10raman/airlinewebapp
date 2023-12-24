package com.venkat.airlinewebapp.exception;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		List<String> errors = Arrays.asList("UnAuthoried- Access denied");
		
		ApiErrorResponse  apiError = ApiErrorResponseBuilder.getInstance()
				.withErrorId("Airline-"+ThreadContext.get("requestid"))
				.forPath(request.getRequestURI())
				.withErrors(errors)
				.withMessage("Unauthorized Access!")
				.withStatus(HttpStatus.UNAUTHORIZED.value())
				.build();
		
		OutputStream os = response.getOutputStream();
		ObjectMapper om = new ObjectMapper();
		om.writeValue(os, apiError);
		os.flush();
		
	}
	
}
