package com.hm.api.admin;

import com.hm.model.AuthController;
import com.hm.repo.ClientRepository;
import com.hm.repo.ModeratorRepository;
import com.hm.repo.UserRepository;
import com.hm.repo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user")
public class UserAdminAPI { //TODO add auth to all methods

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ModeratorRepository moderRepo;
	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private WorkerRepository workerRepo;
	@Autowired
	private AuthController authController;

	@GetMapping("/users")
	public ResponseEntity users(@RequestParam("args") String [] args) {
		return ResponseEntity.ok(userRepo.findAll());
	}

	@GetMapping("/sessions")
	public ResponseEntity sessions() {
		return ResponseEntity.ok(authController.getCachedTokens());
	}



}