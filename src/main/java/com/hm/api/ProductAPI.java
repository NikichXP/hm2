package com.hm.api;

import com.hm.entity.Product;
import com.hm.entity.Worker;
import com.hm.model.AuthController;
import com.hm.repo.GenresHolder;
import com.hm.repo.ProductRepository;
import com.hm.repo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("product")
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
	public ResponseEntity getHat () {
		return ResponseEntity.ok(gh.getCategories());
	}

	@RequestMapping("/offer/{city}")
	public ResponseEntity listOffers (@PathVariable("city") @NotNull String city) {
		return ResponseEntity.ok(prodRepo.listWithOffer(true));
	}

	@RequestMapping("/list/{group}/{city}")
	public ResponseEntity listInCity (@PathVariable("city") @NotNull String cityName, @PathVariable("group") String group) {
		return ResponseEntity.ok(prodRepo.listProductsInCity(cityName, group));
	}



	@RequestMapping("/create")
	public ResponseEntity createProduct (@RequestParam("title") String title, @RequestParam("genre") String genre,
	                                     @RequestParam("cookie") String cookie, @RequestParam("price") double price, @RequestParam(value = "img", required = false) String img) {
		Worker worker = authController.getLoggedToken(cookie, Worker.class);
		Product product = new Product(title, gh.getGenre(genre), price, worker);
		if (img != null) {
			product.setImage(img);
		}
		worker.addProduct(product);
		prodRepo.save(product);
		return ResponseEntity.ok().body(product);
	}
}
