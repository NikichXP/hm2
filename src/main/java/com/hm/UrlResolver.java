package com.hm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@RestController
public class UrlResolver {

	private static final String ROOT = System.getProperty("user.dir") + "/src/main/resources/static/";

	@GetMapping("/index")
	public void home(HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		File f = new File (ROOT + "index.html");
		BufferedReader reader = new BufferedReader(new FileReader(f));
		reader.lines().forEach(x -> {
			try {
				response.getWriter().write(x + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

}
