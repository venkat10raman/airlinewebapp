package com.venkat.airlinewebapp.controller;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//To handle white label error
@Controller
public class ApiErrorController implements ErrorController{

	@RequestMapping(path="/error")
	public void globalError(HttpServletRequest request, HttpServletResponse response) {
		throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
	}
	
}
