package com.hm.api;

import com.google.gson.Gson;
import com.hm.entity.AuthToken;
import com.hm.entity.User;
import com.hm.repo.AuthRepository;
import com.hm.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthAPI {

	@Autowired
	Gson gson;
	@Autowired
	private AuthRepository authRepo;
	@Autowired
	UserRepository userRepo;

	private static Map<String, AuthToken> cachedTokens = new HashMap<>();

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

		cachedTokens.put(token.getSessionID(), token);

		return ResponseEntity.ok(token);
	}

	@RequestMapping("/clean")
	public ResponseEntity cleanup() {
		List<AuthToken> list = new ArrayList<>();
		authRepo.findByTimeoutLessThan(System.currentTimeMillis()).forEach(e -> {
			authRepo.delete(e.getSessionID());
			list.add(e);
			if (cachedTokens.containsKey(e.getSessionID())) {
				cachedTokens.remove(e.getSessionID());
			}
		});
		if (list.isEmpty()) {
			return ResponseEntity.ok("None deleted");
		} else {
			return ResponseEntity.ok(list);
		}
	}

	@RequestMapping("/check/{token}")
	public ResponseEntity testToken (@PathVariable("token") @NotNull String token) {
		if (cachedTokens.containsKey(token)) {
			if (cachedTokens.get(token).getTimeout() < System.currentTimeMillis()) {
				cleanup();
				return ResponseEntity.status(403).body("Session timed out");
			} else {
				return ResponseEntity.ok(cachedTokens.get(token));
			}
		}
		AuthToken au = authRepo.findOne(token);
		if (au == null) {
			return ResponseEntity.status(403).body("Session not found");
		}
		if (au.getTimeout() > System.currentTimeMillis()) {
			cachedTokens.put(au.getSessionID(), au);
			return ResponseEntity.ok(au);
		} else {
			cleanup();
			return ResponseEntity.status(403).body("Session timed out");
		}
	}

	@RequestMapping("/getuser/{token}")
	public ResponseEntity getUser (@PathVariable("token") @NotNull String token) {
		return ResponseEntity.status(470).body("Not yet implemented");
	}

	@RequestMapping("/getemp/{token}")
	public ResponseEntity getEmployee (@PathVariable("token") @NotNull String token) {
		return ResponseEntity.status(470).body("Not yet implemented");
	}

	@RequestMapping("/getClient/{token}")
	public ResponseEntity getClient (@PathVariable("token") @NotNull String token) {
		return ResponseEntity.status(470).body("Not yet implemented");
	}

	@RequestMapping("/test")
	public String test () {
		return "Hi!";
	}



}
