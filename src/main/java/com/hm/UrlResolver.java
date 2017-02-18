package com.hm;

import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//@RestController
public class UrlResolver {

	private static final String ROOT = System.getProperty("user.dir") + "/src/main/resources/static/";

//	@GetMapping("/{file}")
	public void home(HttpServletResponse response, @PathVariable("file") String file) throws IOException {
		response.setCharacterEncoding("UTF-8");
		File f;
		BufferedReader reader;
		try {
			f = new File (ROOT + file);
			reader = new BufferedReader(new FileReader(f));
		} catch (Exception e) {
			f = new File (ROOT + file + ".html");
			reader = new BufferedReader(new FileReader(f));
		}
		reader.lines().forEach(x -> {
			try {
				response.getWriter().write(x + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

//	@GetMapping("/js/{file}")
	public void jsFile(HttpServletResponse response, @PathVariable("file") String file) throws IOException {
		response.setCharacterEncoding("UTF-8");
		File f;
		BufferedReader reader;
		try {
			f = new File (ROOT + "js/" + file);
			reader = new BufferedReader(new FileReader(f));
		} catch (Exception e) {
			f = new File (ROOT + "js/" + file + ".js");
			reader = new BufferedReader(new FileReader(f));
		}
		reader.lines().forEach(x -> {
			try {
				response.getWriter().write(x + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
