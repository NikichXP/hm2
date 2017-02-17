package com.hm.interceptor;

import com.hm.AppLoader;
import com.hm.entity.User;
import com.hm.entity.UserAction;
import com.hm.model.AuthController;
import com.hm.repo.UserActionRepository;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

	private UserActionRepository repo;
	private AuthController authController;

	public LoggerInterceptor() {
		new Thread(() -> {
			try {
				while (AppLoader.ctx == null) {
					Thread.sleep(10);
				}
				repo = AppLoader.ctx.getBean(UserActionRepository.class);
				authController = AppLoader.ctx.getBean(AuthController.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Method method = ((HandlerMethod) handler).getMethod();
		System.out.println(Arrays.toString(method.getAnnotations()));
		System.out.println(Arrays.toString(method.getDeclaringClass().getAnnotations()));
		LogAction log = (method.getAnnotation(LogAction.class) != null) ? method.getAnnotation(LogAction.class)
				: method.getDeclaringClass().getAnnotation(LogAction.class);
		if (log == null) {
			System.out.println("log == null");
			return true;
		}

		String userid = "anon";

		User user = null;
		try {
			user = getUser(request);
		} catch (Exception e) {

		}
		if (user != null) {
			userid = user.getId();
		}

		repo.insert(
				new UserAction(
						request.getRemoteAddr(),
						userid,
						request.getRequestURL().toString(),
						log.value()
				)
		);
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
