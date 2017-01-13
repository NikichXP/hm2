package com.hm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class BasicWebPageDispatcher {

//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String getHome() {
//		return "This is GET-homepage";
//	}

	@Autowired
	AuthAPI authapi;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String redirect() {
		return "redirect:/index.html";
	}

	@RequestMapping("/mapping")
	public ResponseEntity getURIMapping() {
		return ResponseEntity.ok(authapi.getClass().getMethods());
	}

	public static void test() {
		AuthAPI au = new AuthAPI();
		Arrays.stream(au.getClass().getMethods()).forEach(e -> System.out.println(e.getName() + "\t"
				+ Arrays.stream(e.getDeclaredAnnotations()).map(x -> x.toString()).reduce((s1, s2) -> s1 + ", " + s2)));

	}

//	@RequestMapping(value = "/", method = RequestMethod.POST)
//	public String postHome() {
//		return "This is POST-homepage";
//	}
}
