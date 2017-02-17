package com.hm.api.admin;

import com.hm.AppLoader;
import com.hm.api.AuthAPI;
import com.hm.entity.User;
import com.hm.interceptor.Auth;
import com.hm.interceptor.LogAction;
import com.hm.model.AuthController;
import com.hm.repo.ClientRepository;
import com.hm.repo.ModeratorRepository;
import com.hm.repo.UserRepository;
import com.hm.repo.WorkerRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.hm.manualdb.ConnectionHandler.db;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/user")
@Auth("admin")
@LogAction("useradmin")
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

	@GetMapping("/updateUser")
	public ResponseEntity updateUser(@RequestParam("userid") String userid, @RequestParam("data") String[] data) {
		User user = userRepo.findOne(userid);
		AppLoader.ctx.getBean(AuthAPI.class).updateUserInfo(user, data);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/setComment")
	public ResponseEntity setComment(@RequestParam("userid") String userid, @RequestParam("comment") String comment) {
		User user = userRepo.findOne(userid);
		user.setComment(comment);
		userRepo.save(user);
		return ResponseEntity.ok("OK");
	}
	@GetMapping("/worker/promote")
	@LogAction("userpromote")
	public ResponseEntity workerPromoteToPro(@RequestParam("id") String id, @RequestParam("token") String token) {
		db().getCollection("worker").updateOne(Document.parse("{'_id' : '" + id + "'}"), Document.parse("{'$set': {'isPro' : true}}"));
		return ResponseEntity.ok(workerRepo.findOne(id));
	}

	@GetMapping("/loginasother")
	@LogAction("logasother")
	public ResponseEntity loginasother(@RequestParam("token") String token, @RequestParam("target") String target) {
		User user = userRepo.findOne(target);
		return ResponseEntity.ok(authController.auth(user.getMail(), user.getPass()));
	}

	@GetMapping("/users")
	@LogAction("useradmin")
	public ResponseEntity users(@RequestParam(value = "args", required = false) String[] args) {
		List<User> ret = new ArrayList<>();
		ret.addAll(workerRepo.findAll());
		ret.addAll(moderRepo.findAll());
		ret.addAll(clientRepo.findAll());
		return ResponseEntity.ok(ret);
	}

	@GetMapping("/moderators")
	public ResponseEntity moderators(@RequestParam(value = "args", required = false) String[] args) {
		return ResponseEntity.ok(moderRepo.findAll());
	}

	@GetMapping("/workers")
	public ResponseEntity workers(@RequestParam(value = "args", required = false) String[] args) {
		return ResponseEntity.ok(workerRepo.findAll());
	}

	@GetMapping("/clients")
	public ResponseEntity clients(@RequestParam(value = "args", required = false) String[] args) {
		return ResponseEntity.ok(clientRepo.findAll());
	}

	@GetMapping("/sessions")
	public ResponseEntity sessions() {
		return ResponseEntity.ok(authController.getCachedTokens());
	}

}
