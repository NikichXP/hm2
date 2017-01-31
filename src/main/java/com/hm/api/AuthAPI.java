package com.hm.api;

import com.google.gson.Gson;
import com.hm.entity.*;
import com.hm.model.AuthController;
import com.hm.model.UserUtils;
import com.hm.repo.ClientRepository;
import com.hm.repo.ModeratorRepository;
import com.hm.repo.UserRepository;
import com.hm.repo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthAPI {

	@Autowired
	private Gson gson;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ModeratorRepository moderatorRepo;
	@Autowired
	private WorkerRepository workerRepo;
	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private AuthController authController;

	@RequestMapping("/register/{type}")
	public User register(@RequestParam("mail") @NotNull String mail, @RequestParam("pass") String pass,
	                     @PathVariable("type") String type, @RequestParam(value = "img", required = false) String img,
	                     @RequestParam(value = "name", defaultValue = "Some default") String name) {
		if (!mail.matches("[0-9a-zA-Z]{2,}@[0-9a-zA-Z]{2,}\\.[a-zA-Z]{2,5}")) {
			return null;
		}
		pass = UserUtils.encryptPass(mail, pass);
		User u = new User(mail, pass);
		if (img != null) {
			u.setUserImg(img);
		}
		u.setName(name);
		switch (type.toLowerCase()) {
			case "moderator":
				Moderator m = new Moderator(u);
				u = userRepo.save(u);
				moderatorRepo.save(m);
				break;
			case "client":
				Client c = new Client(u);
				u = userRepo.save(u);
				clientRepo.save(c);
				break;
			case "worker":
				Worker w = new Worker(u);
				u = userRepo.save(u);
				workerRepo.save(w);
				break;
			default:
				u = userRepo.save(u);
				break;
		}
		return u;
	}

	@RequestMapping("/auth")
	public ResponseEntity auth(@RequestParam("login") @NotNull String login, @RequestParam("pass") @NotNull String pass) {
		AuthToken token = authController.auth(login, pass);
		if (token == null) {
			ResponseEntity.status(403).body("Wrong credentials");
		}
		return ResponseEntity.ok(token);
	}

	@RequestMapping("/clean")
	public ResponseEntity cleanup() {
		List<AuthToken> list = authController.cleanup();
		if (list.isEmpty()) {
			return ResponseEntity.ok("None deleted");
		} else {
			return ResponseEntity.ok(list);
		}
	}

	@RequestMapping("/check/{token}")
	public ResponseEntity testToken(@PathVariable("token") @NotNull String token) {
		AuthToken au = authController.get(token);
		if (au == null) {
			return ResponseEntity.status(401).body("Session not found");
		}
		if (au.getTimeout() < System.currentTimeMillis()) {
			authController.delete(token);
			cleanup();
			return ResponseEntity.status(403).body("Session timed out");
		}
		return ResponseEntity.ok(au);
	}


	@RequestMapping(value = {"/getUser/{token}", "/getuser/{token}"})
	public ResponseEntity getUser(@PathVariable("token") @NotNull String token) {
		User u = authController.getUser(token);
		if (u == null) {
			return ResponseEntity.status(403).body("Session not found");
		} else {
			return ResponseEntity.ok(u);
		}
	}

	@RequestMapping(value = {"/getEmp/{token}", "/getemp/{token}"})
	public ResponseEntity getEmployee(@PathVariable("token") @NotNull String token) {
		return ResponseEntity.status(470).body("Not yet implemented");
	}

	@RequestMapping(value = {"/getClient/{token}", "/getclient/{token}"})
	public ResponseEntity getClient(@PathVariable("token") @NotNull String token) {
		return ResponseEntity.status(470).body("Not yet implemented");
	}

	@RequestMapping("/test")
	public String test() {
		return "Hi!";
	}

}
