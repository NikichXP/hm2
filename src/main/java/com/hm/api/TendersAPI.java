package com.hm.api;

import com.hm.AppLoader;
import com.hm.entity.Tender;
import com.hm.entity.User;
import com.hm.entity.Worker;
import com.hm.model.AuthController;
import com.hm.repo.BidsRepository;
import com.hm.repo.GenresHolder;
import com.hm.repo.TenderRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/tenders")
@CrossOrigin
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TendersAPI {

	@Autowired
	BidsRepository bidsRepo;
	@Autowired
	TenderRepository biddableRepo;
	@Autowired
	AuthController authController;

	@GetMapping("/{id}")
	public Tender getProduct (@PathVariable("id") String id) {
		return biddableRepo.findOne(id);
	}

	@RequestMapping("/list/all")
	public ResponseEntity listAll(@RequestParam(value = "city", required = false) String city,
	                              @RequestParam(value = "price", defaultValue = "0-100500") String price,
	                              @RequestParam(value = "group", required = false) String group,
	                              @RequestParam(value = "offset", defaultValue = "0") int offset,
	                              @RequestParam(value = "limit", defaultValue = Integer.MAX_VALUE + "") int limit) {
		String[] tmp = price.split("-");

		Stream<Tender> ret = biddableRepo.findByDeadlineAfter(LocalDate.now()).stream()
				.filter(tender -> tender.getPrice() > Integer.parseInt(tmp[0]))
				.filter(tender -> tender.getPrice() < Integer.parseInt(tmp[1]));

		if (city != null) {
			ret = ret.filter(tender -> tender.getCity().equals(city));
		}
		if (group != null) {
			ret = ret.filter(tender -> tender.getGroup().equals(group));
		}

		List<Tender> data = new ArrayList<>();

		ret.forEach(data::add);

		List<Object> a = new ArrayList<>();
		a.add(data.size());

		data = data.stream().skip(offset).limit(limit).collect(Collectors.toList());

		a.addAll(data);

		return ResponseEntity.ok(a);
	}

	@RequestMapping(value = "/create/tender", method = RequestMethod.POST)
	public ResponseEntity<Tender> createTender(@RequestParam("args") String[] args, @RequestParam("text") String text) {
		val data = Arrays.stream(args);
		User user = AppLoader.ctx.getBean(AuthController.class)
				.getUser(data.filter(arg -> arg.startsWith("token"))
						.map(argpair -> argpair.split("=")[1])
						.findAny()
						.orElse(null));
		if (user == null) {
			return ResponseEntity.status(403).body(null);
		}

		Tender product = new Tender();
		if (text != null) {
			product.setDescription(text);
		}

		product.setCreator(user);

		Arrays.stream(args)
				.forEach(arg -> {
					String[] pair = arg.split("=");
					switch (pair[0]) {
						case "deadline":
							product.setDeadline(LocalDate.parse(pair[1]));
							break;
						case "genre":
							if (GenresHolder.isGenreExists(pair[1])) {
								product.setGroup(GenresHolder.getGenre(pair[1]).getGroupName());
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
							break;
						case "hm-agent":
							product.setCreator(null);
							break;
						case "city":
							product.setCity(pair[1]);
							break;
					}
				});

		biddableRepo.save(product);
		return ResponseEntity.ok(product);
	}

	@RequestMapping("/bid/{id}")
	public ResponseEntity bid(@RequestParam("token") String token, @RequestParam("price") int price, @PathVariable("id") String id) {
		Tender prod = biddableRepo.findOne(id);
		Worker worker = authController.getLoggedToken(token, Worker.class);
		if (worker == null || prod == null) {
			return ResponseEntity.status(403).body("Need to validate input data: " + token);
		}
		prod.bid(worker, price);
		biddableRepo.save(prod);
		return ResponseEntity.ok(prod);
	}

}
