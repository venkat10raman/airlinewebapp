package com.venkat.airlinewebapp.exception;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice(basePackages = "com.venkat.airlinewebapp")
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{

	private static final Logger logger = LogManager.getLogger(ApiExceptionHandler.class);
	
	@Override
	protected ResponseEntity<Object> handleNoResourceFoundException(
			NoResourceFoundException ex, 
			HttpHeaders headers,
			HttpStatusCode status, 
			WebRequest request) {
		
		logger.warn("handleNoResourceFoundException...");
		
		List<String> errors = Arrays.asList(ex.getLocalizedMessage());

		HttpServletRequest req = ((ServletWebRequest)request).getRequest();

		ApiErrorResponse  apiError = ApiErrorResponseBuilder.getInstance()
				.withErrorId("Airline-"+LocalDateTime.now(ZoneOffset.UTC))
				.forPath(req.getRequestURI())
				.withErrors(errors)
				.withMessage(ex.getMessage())
				.withStatus(status.value())
				.build();


		return new ResponseEntity<Object>(apiError,headers,status);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, 
			WebRequest request) {

		logger.warn("handleMethodArgumentNotValid....");

		List<String> errors = ex.getBindingResult()
				.getAllErrors().stream()
				.map(e->e.getDefaultMessage())
				.collect(Collectors.toList());

		HttpServletRequest req = ((ServletWebRequest)request).getRequest();

		ApiErrorResponse  apiError = ApiErrorResponseBuilder.getInstance()
				.withErrorId("Airline-"+LocalDateTime.now(ZoneOffset.UTC))
				.forPath(req.getRequestURI())
				.withErrors(errors)
				.withMessage(ex.getMessage())
				.withStatus(status.value())
				.build();


		return new ResponseEntity<Object>(apiError,headers,status);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiErrorResponse> handleResponseStatusException(
			ResponseStatusException ex, 
			HttpServletRequest request){

		logger.warn("handleResponseStatusException....");

		List<String> errors = Arrays.asList(ex.getReason());

		ApiErrorResponse  apiError = ApiErrorResponseBuilder.getInstance()
				.withErrorId("Airline-"+LocalDateTime.now(ZoneOffset.UTC))
				.forPath(request.getRequestURI())
				.withErrors(errors)
				.withMessage(ex.getMessage())
				.withStatus(ex.getStatusCode().value())
				.build();

		return new ResponseEntity<ApiErrorResponse>(apiError,ex.getStatusCode());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
			ConstraintViolationException ex, 
			HttpServletRequest request){

		logger.warn("handleConstraintViolationException....");

		List<String> errors = new ArrayList<>();
		for(ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getMessage());
		}

		ApiErrorResponse  apiError = ApiErrorResponseBuilder.getInstance()
				.withErrorId("Airline-"+LocalDateTime.now(ZoneOffset.UTC))
				.forPath(request.getRequestURI())
				.withErrors(errors)
				.withMessage(ex.getMessage())
				.withStatus(HttpStatus.BAD_REQUEST.value())
				.build();

		return new ResponseEntity<ApiErrorResponse>(apiError,HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleException(
			Exception ex , 
			HttpServletRequest request){
		
		logger.warn("handleException....");

		List<String> errors = Arrays.asList(ex.getMessage());

		ApiErrorResponse  apiError = ApiErrorResponseBuilder.getInstance()
				.withErrorId("Airline-"+LocalDateTime.now(ZoneOffset.UTC))
				.forPath(request.getRequestURI())
				.withErrors(errors)
				.withMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.build();

		return new ResponseEntity<ApiErrorResponse>(apiError,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
