package com.hm.api;

import com.hm.entity.Moderator;
import com.hm.entity.User;
import com.hm.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserAPI {

	@Autowired
	UserRepository userRepo;

	@RequestMapping("/promote")
	public ResponseEntity promoteToEmployee(@RequestParam("mail") String s) {
		User user = userRepo.findByMail(s);
		Moderator emp = new Moderator(user);
		emp.setAccessLevel(100);
		userRepo.delete(user);
		userRepo.insert(emp);
		return ResponseEntity.ok(emp);
	}

}
