package com.hm.api.admin;

import com.hm.repo.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/logs")
public class LogAPI {

	@Autowired
	private UserActionRepository repo;

	@GetMapping("/getLogs")
	public ResponseEntity getLogs() {
		return ResponseEntity.ok(repo.findAll());
	}

	@GetMapping("/getByIP")
	public ResponseEntity getByIP(@RequestParam("ip")String ip) {
		return ResponseEntity.ok(repo.findByIP(ip));
	}

	@GetMapping("/getById")
	public ResponseEntity getById(@RequestParam("userid") String userid) {
		return ResponseEntity.ok(repo.findByUserID(userid));
	}

}
