package com.hm.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessInterceptor implements HandlerInterceptor {

	/*
	TODO A interceptor

	1. Create annotation "AccessGroup".
	Values may be 'all', 'user', 'admin' etc
	2. Create a conveyor-pipeline of identifying the request. return false if forbidden

	 */

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		System.out.println(((HandlerMethod)handler).getBean().toString());
//		System.out.println(((HandlerMethod)handler).getBeanType().toString());
//		System.out.println(((HandlerMethod)handler).getMethod().get));
//		System.out.println(((HandlerMethod)handler).getResolvedFromHandlerMethod().toString());
//		System.out.println("slm: " + ((HandlerMethod)handler).getShortLogMessage());
//		System.out.println(Arrays.toString(((HandlerMethod) handler).getMethodParameters()));
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}
