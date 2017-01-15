package com.hm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicWebPageDispatcher {

	@Autowired
	AuthAPI authapi;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String redirect() {
		return "redirect:/index.html";
	}
}
