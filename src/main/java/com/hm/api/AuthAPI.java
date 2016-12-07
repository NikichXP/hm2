package com.hm.api;

import com.google.gson.Gson;
import com.hm.entity.AuthToken;
import com.hm.entity.Moderator;
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

		add(token);

		return ResponseEntity.ok(token);
	}

	@RequestMapping("/clean")
	public ResponseEntity cleanup() {
		List<AuthToken> list = new ArrayList<>();
		authRepo.findByTimeoutLessThan(System.currentTimeMillis()).forEach(e -> {
			delete(e.getSessionID());
			list.add(e);
		});
		if (list.isEmpty()) {
			return ResponseEntity.ok("None deleted");
		} else {
			return ResponseEntity.ok(list);
		}
	}

	@RequestMapping("/check/{token}")
	public ResponseEntity testToken (@PathVariable("token") @NotNull String token) {

		AuthToken au = get(token);

		if (au == null) {
			return ResponseEntity.status(401).body("Session not found");
		}

		if (au.getTimeout() < System.currentTimeMillis()) {
			delete(token);
			cleanup();
			return ResponseEntity.status(403).body("Session timed out");
		}

		return ResponseEntity.ok(au);
	}

	private User getUser(@NotNull String token) {
		AuthToken au = get(token);
		if (au.getTimeout() > System.currentTimeMillis()) {
			return au.getUser();
		} else {
			cleanup();
			return null;
		}
	}

	@RequestMapping(value = {"/getUser/{token}", "/getuser/{token}"})
	public ResponseEntity getUserWeb (@PathVariable("token") @NotNull String token) {
		User u = getUser(token);
		System.out.println(u.toString());
		System.out.println((Moderator)(u));
		if (u == null) {
			return ResponseEntity.status(403).body("Session not found");
		} else {
			return ResponseEntity.ok(u);
		}
	}

	@RequestMapping(value = {"/getEmp/{token}", "/getemp/{token}"})
	public ResponseEntity getEmployee (@PathVariable("token") @NotNull String token) {
		return ResponseEntity.status(470).body("Not yet implemented");
	}

	@RequestMapping(value = {"/getClient/{token}", "/getclient/{token}"})
	public ResponseEntity getClient (@PathVariable("token") @NotNull String token) {
		return ResponseEntity.status(470).body("Not yet implemented");
	}

	@RequestMapping("/test")
	public String test () {
		return "Hi!";
	}

	private AuthToken get (@NotNull String token) {
		AuthToken ret = cachedTokens.get(token);
		if (ret != null) {
			return ret;
		}
		ret = authRepo.findOne(token);
		return ret; //can be null!
	}

	private void add (AuthToken token) {
		authRepo.save(token);
		cachedTokens.put(token.getSessionID(), token);
	}

	private void delete (String token) {
		cachedTokens.remove(token);
		authRepo.delete(token);
	}

}
