package com.hm.api;

import com.google.gson.Gson;
import com.hm.entity.AuthToken;
import com.hm.entity.User;
import com.hm.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthAPI {

	@Autowired
	Gson gson;
	@Autowired
	private AuthRepository authRepo;
	@Autowired
	UserRepository userRepo;

	@RequestMapping("/register")
	public User register (@RequestParam("mail") @NotNull String mail, @RequestParam("pass") String pass) {

		if (!mail.matches("[0-9a-zA-Z]{2,}@[0-9a-zA-Z]{2,}\\.[a-zA-Z]{2,5}")) {
			return null;
		}

		User u = new User(mail, pass);
		u = userRepo.save(u);
		return u;
	}

	@RequestMapping ("/auth")
	public ResponseEntity auth (@RequestParam("login") @NotNull String login, @RequestParam("pass") @NotNull String pass) {
		User user = userRepo.findByMailAndPass(login, pass);
		if (user == null) {
			ResponseEntity.status(403).body("Wrong credentials");
		}
		AuthToken token = new AuthToken(user);
		authRepo.save(token);

		return ResponseEntity.ok(token);
	}

	@RequestMapping("/clean")
	public ResponseEntity cleanup() {
		List<AuthToken> list = new ArrayList<>();
		authRepo.findByTimeoutLessThan(System.currentTimeMillis()).forEach(e -> {
			authRepo.delete(e.getSessionID());
			list.add(e);
		});
		if (list.isEmpty()) {
			return ResponseEntity.ok("None deleted");
		} else {
			return ResponseEntity.ok(list);
		}
	}

	@RequestMapping("/test")
	public String test () {
		return "Hi!";
	}



}
