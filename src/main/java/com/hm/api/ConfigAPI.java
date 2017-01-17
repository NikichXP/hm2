package com.hm.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigAPI {

	@GetMapping("city")
	public ResponseEntity listCities () {
		return ResponseEntity.ok(new String []{"Киев", "Львов", "Одесса"});
	}

}
