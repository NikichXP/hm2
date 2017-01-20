package com.hm.api;

import com.hm.repo.BiddableProductRepository;
import com.hm.repo.BidsRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RequestMapping("/bids")
@RestController
public class BidsAPI {

	@Autowired
	BidsRepository bidsRepo;
	@Autowired
	BiddableProductRepository biddableRepo;

	@RequestMapping("/list/all")
	public ResponseEntity listAll () {
		return ResponseEntity.ok(biddableRepo.findAll());
	}

	@RequestMapping(value = "/create/bid", method = RequestMethod.POST)
	public ResponseEntity createBid (@RequestParam("args") String[] args) {
		val data = Arrays.stream(args);
//		User user = AppLoader.ctx.getBean(AuthController.class)
//				.getUser(data.filter(arg -> arg.startsWith("token"))
//				.findAny()
//				.orElse(null));
//		if (user == null) {
////			return ResponseEntity.status(403).body("Need authorize"); TODO Remove test line
//		}

		return ResponseEntity.ok(args);
	}

}
