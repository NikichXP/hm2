package com.hm.api;

import com.hm.entity.*;
import com.hm.repo.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserAPI {

	@Autowired
	UserRepository userRepo;
	@Autowired
	WorkerRepository workRepo;
	@Autowired
	ModeratorRepository moderatorRepo;

	@RequestMapping("/setEmployee")
	public ResponseEntity promoteToEmployee(@RequestParam("mail") String s) {
		User user = userRepo.findByMail(s);
		Moderator emp = new Moderator(user);
		emp.setAccessLevel(100);
		userRepo.delete(user);
		moderatorRepo.insert(emp);
		return ResponseEntity.ok(emp);
	}

	@RequestMapping("/setWorker/{userId}")
	public ResponseEntity promoteToWorker (@PathVariable("userId") String userId) {
		User user = userRepo.findOne(userId);
		Worker worker = new Worker(user);
		userRepo.delete(user);
		workRepo.save(worker);
		return ResponseEntity.ok(worker);
	}

	/**
	 * @return Shuffled array of users
	 */
	@GetMapping("/getProUsers")
	public ResponseEntity getProUsers(@RequestParam(value = "city", required = false) String city) {
		if (city == null) {
			return ResponseEntity.ok(workRepo.getPro().sorted((x1, x2) -> (Math.random() > 0.5) ? 1 : -1).limit(4).toArray());
		}
		return ResponseEntity.ok(workRepo.getPro(city).sorted((x1, x2) -> (Math.random() > 0.5) ? 1 : -1).limit(4).toArray());
	}

}
