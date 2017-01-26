package com.hm.api.admin;

import com.hm.entity.Worker;
import com.hm.model.AuthController;
import com.hm.repo.ClientRepository;
import com.hm.repo.ModeratorRepository;
import com.hm.repo.UserRepository;
import com.hm.repo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/user")
public class UserAdminAPI { //TODO add auth to all methods

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ModeratorRepository moderRepo;
	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private WorkerRepository workerRepo;
	@Autowired
	private AuthController authController;

	@GetMapping("/worker/promote")
	public ResponseEntity workerPromoteToPro (@RequestParam("id") String id, @RequestParam("token") String token) {
		Worker w = workerRepo.findOne(id);
//		db().getCollection("worker").updateOne(Document.parse("{'_id' : '" + id + "'}"), Document.parse("{'isPro' : 'true'}"));
		workerRepo.delete(id);
		workerRepo.insert(w);
		return ResponseEntity.ok(workerRepo.findOne(id));
	}

	@GetMapping("/users")
	public ResponseEntity users(@RequestParam(value = "args", required = false) String [] args) {
		return ResponseEntity.ok(userRepo.findAll());
	}

	@GetMapping("/moderators")
	public ResponseEntity moderators(@RequestParam(value = "args", required = false) String [] args) {
		return ResponseEntity.ok(moderRepo.findAll());
	}

	@GetMapping("/workers")
	public ResponseEntity workers(@RequestParam(value = "args", required = false) String [] args) {
		return ResponseEntity.ok(workerRepo.findAll());
	}

	@GetMapping("/clients")
	public ResponseEntity clients(@RequestParam(value = "args", required = false) String [] args) {
		return ResponseEntity.ok(clientRepo.findAll());
	}

	@GetMapping("/sessions")
	public ResponseEntity sessions() {
		return ResponseEntity.ok(authController.getCachedTokens());
	}



}
