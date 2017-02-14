package com.hm.config;

import com.hm.interceptor.LoggerInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.hm")
public class WebApplicationConfig extends WebMvcConfigurerAdapter {

	private static final String[] RESOURCE_LOCATIONS = {
			"classpath:/META-INF/resources/", "classpath:/resources/",
			"classpath:/static/", "classpath:/public/", "classpath:/resources/static/",
			"classpath:/src/main/resources/static/"
	};

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/api/**")) {
			registry.addResourceHandler("/**").addResourceLocations(
					RESOURCE_LOCATIONS);
		}
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**");
//		registry.addInterceptor(new AccessInterceptor()).addPathPatterns("/private/**");;
	}

}