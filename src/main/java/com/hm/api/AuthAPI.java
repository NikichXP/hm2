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
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
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
	                     @RequestParam(value = "city", defaultValue = "Киев") String city,
	                     @RequestParam(value = "name", defaultValue = "Some default") String name) {
		if (!mail.matches("[0-9a-zA-Z]{2,}@[0-9a-zA-Z]{2,}\\.[a-zA-Z]{2,5}")) {
			return null;
		}
		pass = UserUtils.encryptPass(mail, pass);
		User u = new User(mail, pass);
		if (img != null) {
			u.setUserImg(img);
		}
		u.setId(ConfigAPI.getNextUserId()+"");
		u.setName(name);
		u.setCity(city);
		u.setRegDate(LocalDate.now().toString());
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
				throw new IllegalArgumentException("This arg is impossible: " + type);
		}
		return u;
	}

	public void updateUserInfo (User user, String[] data) {
		for (String datapair : data) {
			String[] pair = datapair.split("=");
			switch(pair[0].toLowerCase()) {
				case "userimg":
					user.setUserImg(pair[1]);
					break;
				case "name":
					user.setName(pair[1]);
					break;
				case "city":
					user.setCity(pair[1]);
					break;
				case "phone":
					user.setPhone(pair[1]);
					break;
			}
		}
		userRepo.save(user);
		switch (user.getEntityClassName().toLowerCase()) {
			case "moderator":
				Moderator m = moderatorRepo.findOne(user.getId());
				user.cloneTo(m);
				moderatorRepo.save(m);
				break;
			case "client":
				Client c = clientRepo.findOne(user.getId());
				user.cloneTo(c);
				clientRepo.save(c);
				break;
			case "worker":
				Worker w = workerRepo.findOne(user.getId());
				user.cloneTo(w);
				workerRepo.save(w);
				break;
			default:
				throw new IllegalArgumentException("This arg is impossible: " + user.getEntityClassName()); //WHAT?
		}
	}

	@GetMapping("/updateInfo")
	public ResponseEntity updateInfo(@RequestParam("token") String token, @RequestParam("data") String[] data) {
		User user = userRepo.findOne(authController.get(token).getUser().getId());
		if (user == null) {
			return ResponseEntity.status(403).body("Wrong data");
		}
		try {
			updateUserInfo(user, data);
		} catch (IllegalArgumentException e1) {
			return ResponseEntity.status(401).body("Wrong argument");
		} catch (Exception e) {
			return ResponseEntity.status(401).body("Error parsing data");
		}
		return ResponseEntity.ok(user);
	}

	@PostMapping("/updateDescription")
	public ResponseEntity updateDescription (@RequestParam("text") String text, @RequestParam("token") String token) {
		User user = userRepo.findOne(authController.get(token).getUser().getId());
		user.setDescription(text);
		userRepo.save(user);
		switch (user.getEntityClassName().toLowerCase()) {
			case "moderator":
				Moderator m = moderatorRepo.findOne(user.getId());
				m.setDescription(text);
				moderatorRepo.save(m);
				break;
			case "client":
				Client c = clientRepo.findOne(user.getId());
				c.setDescription(text);
				clientRepo.save(c);
				break;
			case "worker":
				Worker w = workerRepo.findOne(user.getId());
				w.setDescription(text);
				workerRepo.save(w);
				break;
			default:
				throw new IllegalArgumentException("This arg is impossible: " + user.getEntityClassName()); //WHAT?
		}
		return ResponseEntity.ok(user);
	}

	@PostMapping("/changePass")
	public ResponseEntity changePass(@RequestParam("token") String token, @RequestParam("userId") String id,
	                                 @RequestParam("oldpass") String oldpass, @RequestParam("newpass") String newpass) {
		if (!Objects.equals(authController.get(token).getUser().getId(), id)) {
			return ResponseEntity.status(403).body("Wrong id (admins reset not yet finished");
		}
		User user = userRepo.findOne(id);
		if (!UserUtils.encryptPass(user.getMail(), oldpass).equals(user.getPass())) {
			return ResponseEntity.status(403).body("Wrong old password");
		}
		user.setPass(UserUtils.encryptPass(user.getMail(), newpass));
		userRepo.save(user);
		return ResponseEntity.ok(null);
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
