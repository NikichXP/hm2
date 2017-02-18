package com.hm;

import com.hm.api.ConfigAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication

public class AppLoader {

	public static final ApplicationContext ctx = SpringApplication.run(AppLoader.class);

	public static void main(String[] args) {
		while (ctx == null) {}
		ConfigAPI.updateNexts();
		System.out.println("Seems like all running normal");
	}



}


