package com.hm.api;

import com.hm.entity.*;
import com.hm.model.AuthController;
import com.hm.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class UserAPI {

	@Autowired
	private AuthController authController;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private WorkerRepository workerRepo;
	@Autowired
	private ModeratorRepository moderatorRepo;
	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private ProductAPI productAPI;

	@GetMapping("/portfolio/{id}")
	public ResponseEntity portfolio(@PathVariable("id") String userId,
	                                @RequestParam(value = "genre", required = false) String genre,
	                                @RequestParam(value = "count", required = false) Integer count) {
		try {
			return ResponseEntity.ok(productAPI
					.getUserProducts(userId).getBody().stream()
					.filter(p -> genre == null || p.getGenreName().equals(genre))
					.flatMap(prod -> prod.getPhotos().stream())
					.sorted((a, b) -> -1) //to get last of 'em //TODO TEST this
					.limit((count == null || count < 1) ? 0x7FFFFFFF : count)
					.collect(Collectors.toList()));
		} catch (Exception e) {
			return ResponseEntity.status(404).body("Wrong data");
		}
	}

	@GetMapping("/search")
	public ResponseEntity search(@RequestParam(value = "group", required = false) String group,
	                             @RequestParam(value = "name", required = false) String name,
	                             @RequestParam(value = "limit", required = false) Integer limit,
	                             @RequestParam(value = "offset", required = false) Integer offset) {
		List<Worker> list;
		if (group == null) {
			list = workerRepo.findAll();
		} else {
			list = workerRepo.getByProfession(group).collect(Collectors.toList());
		}
		List<Object> ret = new ArrayList<>();
		ret.add(list.size());
		ret.add(list.stream()
				.filter(u -> name == null || u.getName().contains(name))
				.sorted(Comparator.comparingInt(u -> Integer.parseInt(u.getId())))
				.skip((offset != null) ? offset : 0)
				.limit((limit != null) ? limit : 0x7FFFFFFF)
				.collect(Collectors.toList())
		);
		return ResponseEntity.ok(ret);
	}

	@GetMapping("/changeAvatar")
	public ResponseEntity changeAvatar(@RequestParam("token") String token, @RequestParam("img") String img) {
		User user = authController.getUser(token);
		if (user == null) {
			return ResponseEntity.ok("No user found");
		}
		user = userRepo.findOne(user.getId());
		user.setUserImg(img);
		userRepo.save(user);
		switch (user.getEntityClassName().toLowerCase()) {
			case "moderator":
				Moderator m = moderatorRepo.findOne(user.getId());
				m.setUserImg(img);
				moderatorRepo.save(m);
				break;
			case "client":
				Client c = clientRepo.findOne(user.getId());
				c.setUserImg(img);
				clientRepo.save(c);
				break;
			case "worker":
				Worker w = workerRepo.findOne(user.getId());
				w.setUserImg(img);
				workerRepo.save(w);
				break;
			default:
				throw new IllegalArgumentException("This arg is impossible: " + user.getEntityClassName()); //WHAT?
		}
		return ResponseEntity.ok("Done");
	}

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
	public ResponseEntity promoteToWorker(@PathVariable("userId") String userId) {
		User user = userRepo.findOne(userId);
		Worker worker = new Worker(user);
		userRepo.delete(user);
		workerRepo.save(worker);
		return ResponseEntity.ok(worker);
	}

	@RequestMapping("/getUser")
	public ResponseEntity getUserById(@RequestParam("id") String id) {
		User user = userRepo.findOne(id);
		if (user == null) {
			return ResponseEntity.status(404).body("User not found");
		}
		switch (user.getEntityClassName().toLowerCase()) {
			case "worker":
				user = workerRepo.findOne(user.getId());
				break;
			case "client":
				user = userRepo.findOne(user.getId());
				break;
			case "moderator":
				user = moderatorRepo.findOne(user.getId());
				break;
		}
		user.setPass(null);
		return ResponseEntity.ok(user);
	}


	/**
	 * @return Shuffled array of users
	 */
	@GetMapping("/getProUsers")
	public ResponseEntity getProUsers(@RequestParam(value = "city", required = false) String city) {
		List<Worker> list;
		if (city == null) {
			list = workerRepo.getPro();
		} else {
			list = workerRepo.getPro(city);
		}
		Random rand = new Random();
		Worker[] ret = new Worker[4];
		for (int i = 0; i < 4; i++) {
			ret[i] = list.get(rand.nextInt(list.size()));
		}
		return ResponseEntity.ok(ret);
	}

}
