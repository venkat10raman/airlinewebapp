package com.venkat.airlinewebapp.aop;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoggingInterceptor implements HandlerInterceptor {
	
	private static final Logger logger = LogManager.getLogger(LoggingInterceptor.class);
	
	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws Exception {
		ThreadContext.put("requestId", UUID.randomUUID().toString());
		logger.info( "PreHandle method is Calling");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		ThreadContext.clearAll();
		logger.info( "PostHandle method is Calling");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.info( "Request and Response is completed");
	}

}
