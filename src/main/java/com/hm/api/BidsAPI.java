package com.hm.api;

import com.hm.AppLoader;
import com.hm.entity.*;
import com.hm.model.AuthController;
import com.hm.repo.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

@RequestMapping("/bids")
@RestController
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidsAPI {

	@Autowired
	BidsRepository bidsRepo;
	@Autowired
	BiddableProductRepository biddableRepo;
	@Autowired
	AuthController authController;

	@RequestMapping("/list/all")
	public ResponseEntity listAll() {
		return ResponseEntity.ok(biddableRepo.findAll());
	}

	@RequestMapping(value = "/create/bid", method = RequestMethod.POST)
	public ResponseEntity createBid(@RequestParam("args") String[] args, @RequestParam("text") String text) {
		val data = Arrays.stream(args);
		User user = AppLoader.ctx.getBean(AuthController.class)
				.getUser(data.filter(arg -> arg.startsWith("token"))
						.map(argpair -> argpair.split("=")[1])
						.findAny()
						.orElse(null));
		if (user == null) {
			return ResponseEntity.status(403).body("Need authorize");
		}

		BiddableProduct product = new BiddableProduct();
		if (text != null) {
			product.setDescription(text);
		}

		Arrays.stream(args)
				.forEach(arg -> {
					String[] pair = arg.split("=");
					switch (pair[0]) {
						case "deadline":
							product.setDeadline(LocalDate.parse(pair[1]));
							break;
						case "genre":
							if (GenresHolder.isGenreExists(pair[1])) {
								product.setGenre(pair[1]);
							}
							break;
						case "title":
							product.setTitle(pair[1]);
							break;
						case "price":
							product.setPrice(Integer.parseInt(pair[1]));
							break;
						case "workingHours":
							product.setWorkingHours(Integer.parseInt(pair[1]));
					}
				});

		biddableRepo.save(product);
		return ResponseEntity.ok(product);
	}

	@RequestMapping("/bid/{id}")
	public ResponseEntity bid(@RequestParam("token") String token, @RequestParam("price") int price, @PathVariable("id") String id) {
		BiddableProduct prod = biddableRepo.findOne(id);
		Worker worker = authController.getLoggedToken(token, Worker.class);
		if (worker == null || prod == null) {
			return ResponseEntity.status(403).body("Need to validate input data: " + token);
		}
		val node = new BiddableProduct.Node();
		node.setBid(price);
		node.setUserId(worker.getId());
		node.setUserName(worker.getName());
		node.setUserImg(worker.getUserImg());
		prod.getBidders().add(node);
		biddableRepo.save(prod);
		return ResponseEntity.ok(new Object[]{prod, node});
	}

}
