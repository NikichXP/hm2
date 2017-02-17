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

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Method method = ((HandlerMethod) handler).getMethod();
		Auth auth = (method.getAnnotation(Auth.class) != null) ? method.getAnnotation(Auth.class) :
				method.getDeclaringClass().getAnnotation(Auth.class);
		if (auth == null) {
			System.out.println("auth == null");
			return true;
		}

		User user = null;
		try {
			user = getUser(request);
		} catch (Exception e) {
			return false;
		}
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
		System.out.println("Check auth: " + ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class).value());
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
