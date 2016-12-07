package com.hm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static com.hm.manualdb.ConnectionHandler.db;

@SpringBootApplication
public class MyAppLoader {

	public static void main(String[] args) {
		db();
		ApplicationContext ctx = SpringApplication.run(MyAppLoader.class, args);
		System.out.println("Seems like all running normal");
	}

}