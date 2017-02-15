package com.hm.interceptor;

import com.hm.AppLoader;
import com.hm.entity.User;
import com.hm.model.AuthController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AccessInterceptor extends HandlerInterceptorAdapter {

	private AuthController authController;

	public AccessInterceptor() {
		new Thread(() -> {
			try {
				while (AppLoader.ctx == null) {
					Thread.sleep(10);
				}
				authController = AppLoader.ctx.getBean(AuthController.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}


	/*
	TODO An interceptor

	1. Create annotation "AccessGroup".
	Values may be 'all', 'user', 'admin' etc
	2. Create a conveyor-pipeline of identifying the request. return false if forbidden

	cookie - sessionId
	 */

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Method method = ((HandlerMethod) handler).getMethod();
		Auth auth = (method.getAnnotation(Auth.class) != null) ? method.getAnnotation(Auth.class) : method.getClass().getAnnotation(Auth.class);
		if (auth == null) {
			return true;
		}

		User user = getUser(request);
		if (user == null) {
			return false;
		}

		switch (auth.value()) {
			case "admin":
			case "moderator":
				if (user.getEntityClassName().compareToIgnoreCase("moderator") == 0) {
					return true;
				}
				break;
		}

//		System.out.println(((HandlerMethod)handler).getBean().toString());
//		System.out.println(((HandlerMethod)handler).getBeanType().toString());
		System.out.println(((HandlerMethod) handler).getMethod().getAnnotation(Auth.class).value());
//		System.out.println(((HandlerMethod)handler).getResolvedFromHandlerMethod().toString());
//		System.out.println("slm: " + ((HandlerMethod)handler).getShortLogMessage());
//		System.out.println(Arrays.toString(((HandlerMethod) handler).getMethodParameters()));
		return true;
	}

	private User getUser(HttpServletRequest request) {
		String token = Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals("sessionId"))
				.map(Cookie::getValue)
				.findAny().orElseGet(() -> (request.getParameter("token") != null) ? request.getParameter("token") : request.getParameter("sessionId"));
		return (token == null) ? null : authController.getUser(token);
	}

}
