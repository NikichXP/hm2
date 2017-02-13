package com.hm.api;

import com.hm.entity.Moderator;
import com.hm.entity.User;
import com.hm.entity.Worker;
import com.hm.repo.ModeratorRepository;
import com.hm.repo.UserRepository;
import com.hm.repo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserAPI {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private WorkerRepository workerRepo;
	@Autowired
	private ModeratorRepository moderatorRepo;
	@Autowired
	private ProductAPI productAPI;

	@GetMapping("/portfolio/{id}")
	public ResponseEntity portfolio(@PathVariable("id") String userId,
	                                @RequestParam(value = "genre", required = false) String genre,
	                                @RequestParam(value = "count", required = false) Integer count) {
		try {
			return ResponseEntity.ok(productAPI
					.getUserProducts(userId).getBody().stream()
					.peek(System.out::println)
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
				.sorted(Comparator.comparingInt(u -> Integer.parseInt(u.getId())))
				.skip((offset != null) ? offset : 0)
				.limit((limit != null) ? limit : 0x7FFFFFFF)
				.collect(Collectors.toList())
		);
		return ResponseEntity.ok(ret);
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
		if (city == null) {
			return ResponseEntity.ok(workerRepo.getPro().sorted((x1, x2) -> (Math.random() > 0.5) ? 1 : -1).limit(4).toArray());
		}
		return ResponseEntity.ok(workerRepo.getPro(city).sorted((x1, x2) -> (Math.random() > 0.5) ? 1 : -1).limit(4).toArray());
	}

}
