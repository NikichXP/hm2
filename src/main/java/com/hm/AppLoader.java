package com.hm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
/**
 * FIXME App sends all data in JSON, this is unappliable! Should not send passwords in JSON
 */
public class AppLoader {

	public static final ApplicationContext ctx = SpringApplication.run(AppLoader.class, new String[] {});

	public static void main(String[] args) {
		while (ctx == null) {}
		System.out.println("Seems like all running normal");
	}

}


