package com.hm;

import com.hm.api.ConfigAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@RestController //just for mainpage
public class AppLoader {

	public static final ApplicationContext ctx = SpringApplication.run(AppLoader.class, new String[] {});

	public static void main(String[] args) {
		while (ctx == null) {}
		ConfigAPI.updateNexts();
		System.out.println("Seems like all running normal");
	}

	@GetMapping("/")
	public void home(HttpServletResponse response) throws IOException {
		response.sendRedirect("/index.html");
	}

}


