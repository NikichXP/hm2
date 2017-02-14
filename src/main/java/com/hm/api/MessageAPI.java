package com.hm.api;

import com.hm.entity.Message;
import com.hm.entity.User;
import com.hm.model.AuthController;
import com.hm.repo.MessageRepository;
import com.hm.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/message")
public class MessageAPI {

	@Autowired
	private AuthController authController;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private MessageRepository messageRepo;

	@PostMapping("/send")
	public ResponseEntity sendMessage (@RequestParam("token") String token,
	                                   @RequestParam("targetid") String targetid,
	                                   @RequestParam("theme") String theme,
	                                   @RequestParam("text") String text) {
		User user = authController.getUser(token);
		if (user == null) {
			return ResponseEntity.status(403).body("Need reauth");
		}
		User target = userRepo.findOne(targetid);
		if (target == null) {
			return ResponseEntity.status(401).body("Wrong user id");
		}
		Message message = new Message();
		message.setDelegates(user, target);
		message.setTheme(theme);
		message.setText(text);
		messageRepo.save(message);
		return ResponseEntity.ok(message);
	}

	@GetMapping("/inbox")
	public ResponseEntity getInbox (@RequestParam("token") String token) {
		User user = authController.getUser(token);
		if (user == null) {
			return ResponseEntity.status(403).body("Need reauth");
		}
		List<Message> list = messageRepo.getMessagesByRecieptId(user.getId());
		list.sort((m1, m2) -> (m1.isSeen() == m2.isSeen()) ? m1.getSent().compareTo(m2.getSent()) : (m1.isSeen()) ? 1 : -1);
		return ResponseEntity.ok(list);
	}

	@GetMapping("/seen")
	public ResponseEntity seenMessage (@RequestParam("token") String token, @RequestParam("messageid") String messageid) {
		String userid = authController.getUser(token).getId();
		Message msg = messageRepo.findOne(messageid);
		if (!msg.getRecieptId().equals(userid)) {
			return ResponseEntity.status(403).body("Need reauth");
		}
		msg.setSeen(true);
		messageRepo.save(msg);
		return ResponseEntity.ok(msg);
	}

	@GetMapping("/delete")
	public ResponseEntity delete (@RequestParam("token") String token, @RequestParam("messageid") String messageid) {
		String userid = authController.getUser(token).getId();
		Message msg = messageRepo.findOne(messageid);
		if (!msg.getRecieptId().equals(userid)) {
			return ResponseEntity.status(403).body("Need reauth");
		}
		messageRepo.delete(msg.getId());
		return ResponseEntity.ok("Deleted.");
	}
}
