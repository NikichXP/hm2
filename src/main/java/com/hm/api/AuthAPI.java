package com.hm.api;

import com.google.gson.Gson;
import com.hm.dao.UserDAO;
import com.hm.entity.AuthToken;
import com.hm.entity.User;
import com.hm.repo.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import static com.hm.dao.db.ConnectionHandler.db;

@RestController
@RequestMapping("/auth")
public class AuthAPI {

	@Autowired
	Gson gson;

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
	public AuthToken auth (@RequestParam("login") @NotNull String login, @RequestParam("pass") @NotNull String pass) {
		User user = UserDAO.getUser(login, pass);
		AuthToken token = new AuthToken(user);

		db().getCollection("auth").insertOne(Document.parse(gson.toJson(token)));

		return token;
	}




	@RequestMapping("/test")
	public String test () {
		return "Hi!";
	}



}
