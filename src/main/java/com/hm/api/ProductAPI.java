package com.hm.api;

import com.hm.entity.*;
import com.hm.model.AuthController;
import com.hm.repo.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.stream.*;

@RestController
@RequestMapping("/product")
public class ProductAPI {

	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private GenresHolder gh;
	@Autowired
	private AuthController authController;
	@Autowired
	private WorkerRepository workerRepo;

	@RequestMapping("/categories")
	public ResponseEntity getHat() {
		return ResponseEntity.ok(gh.getCategories());
	}

	@RequestMapping("/offer")
	public ResponseEntity listProducts(@RequestParam(value = "city", required = false) String city,
	                                   @RequestParam(value = "shuffle", required = false) Boolean shuffle,
	                                   @RequestParam(value = "genre", required = false) String genre,
	                                   @RequestParam(value = "limit", required = false) int limit) {
		Stream<Product> stream;
		if (city == null && genre == null) {
			stream = prodRepo.listCustomQuery("offeredPrice", true).stream();
		} else if (city != null && genre != null) {
			stream = prodRepo.listCustomThreeArgQuery("offeredPrice", true, "city", city, "genreName", genre).stream();
		} else {
			if (city != null) {
				stream = prodRepo.listCustomTwoArgQuery("offeredPrice", true, "city", city).stream();
			} else {
				stream = prodRepo.listCustomTwoArgQuery("offeredPrice", true, "genreName", genre).stream();
			}
		}

		if (shuffle) {
			stream = stream.sorted((x1, x2) -> (int) (Math.random() * 10 - 5));
		}
		if (limit != 0) {
			stream = stream.limit(limit);
		}
		return ResponseEntity.ok(stream.collect(Collectors.toList()));
	}

	@RequestMapping("/list/{group}/{city}")
	public ResponseEntity listInCity(@PathVariable("city") @NotNull String cityName, @PathVariable("group") String group) {
		return ResponseEntity.ok(prodRepo.listProductsInCity(cityName, group));
	}


	@RequestMapping("/create")
	public ResponseEntity createProduct(@RequestParam("title") String title, @RequestParam("genre") String genre,
	                                    @RequestParam("cookie") String cookie, @RequestParam("price") double price, @RequestParam(value = "img", required = false) String img) {
		val worker = authController.getLoggedToken(cookie, Worker.class);
		val product = new Product(title, gh.getGenre(genre), price, worker);
		if (img != null) {
			product.setImage(img);
		}
		worker.addProduct(product);
		prodRepo.save(product);
		return ResponseEntity.ok().body(product);
	}
}
