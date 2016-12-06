package com.hm.api;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicWebPageDispatcher {






	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getHome() {

		return "This is GET-homepage";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String postHome() {
		return "This is POST-homepage";
	}
}
