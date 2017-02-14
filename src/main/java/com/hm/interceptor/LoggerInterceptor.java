package com.hm.interceptor;

import com.hm.AppLoader;
import com.hm.entity.UserAction;
import com.hm.repo.UserActionRepository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggerInterceptor implements HandlerInterceptor {

	private UserActionRepository repo;

	public LoggerInterceptor() {
		new Thread(() -> {
			try {
				while (AppLoader.ctx == null) {
					Thread.sleep(10);
				}
				repo = AppLoader.ctx.getBean(UserActionRepository.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
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
