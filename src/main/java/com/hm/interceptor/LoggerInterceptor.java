package com.hm.interceptor;

import com.hm.AppLoader;
import com.hm.entity.UserAction;
import com.hm.repo.UserActionRepository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggerInterceptor implements HandlerInterceptor {

	private static UserActionRepository repo;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		if (repo == null) {
			repo = AppLoader.ctx.getBean(UserActionRepository.class);
		}
		System.out.println(" -------------------------------- ");
		System.out.println(request.getRequestURL());
		repo.insert(
				new UserAction(
						request.getRemoteAddr(),
						request.getRequestURL().toString()
				)
		);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}
