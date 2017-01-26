package com.hm.api;

import com.hm.entity.Product;
import com.hm.entity.Worker;
import com.hm.model.AuthController;
import com.hm.repo.GenresHolder;
import com.hm.repo.ProductRepository;
import com.hm.repo.WorkerRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@RestController
@CrossOrigin
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
	public ResponseEntity<java.util.Collection<com.hm.entity.Category>> getHat() {
		return ResponseEntity.ok(GenresHolder.getCategories());
	}

	/**
	 * List products with discounts
	 * @param date - up to this date offers shown
	 * @param offset - objects to skip
	 */
	@RequestMapping("/offer")
	public ResponseEntity<List<Object>> listProducts(@RequestParam(value = "city", required = false) String city,
	                                                 @RequestParam(value = "shuffle", required = false) Boolean shuffle,
	                                                 @RequestParam(value = "genre", required = false) String genre,
	                                                 @RequestParam(value = "group", required = false) String group,
	                                                 @RequestParam(value = "limit", required = false) Integer limit,
	                                                 @RequestParam(value = "date", required = false) String date,
	                                                 @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		HashMap<String, Object> args = new HashMap<>();

		if (date == null) {
			date = "2099-12-31";
		}
		LocalDate expiration = LocalDate.parse(date);

		args.put("offeredPrice", true);
		if (city != null) {
			args.put("city", city);
		}
		if (group != null) {
			args.put("groupName", group);
		} else if (genre != null) {
			args.put("genreName", genre);
		}

		Method method = stream(prodRepo.getClass().getDeclaredMethods())
				.filter(meth -> meth.getName().equals("listCustom" + args.size() + "ArgQuery"))
				.findFirst()
				.orElseGet(() -> null);

		Object[] queryArgs = new Object[args.size() * 2];
		int ptr = 0;
		for (Object o : args.keySet()) {
			queryArgs[ptr] = o;
			queryArgs[ptr + 1] = args.get(o);
			ptr += 2;
		}
		Stream<Product> stream = (Stream<Product>) method.invoke(prodRepo, queryArgs);

//		stream = stream.peek(prod -> {
//			if (prod.getExpirationDate().isBefore(LocalDate.now())) {
//				checkOffer(prod);
//			}
//		});

		stream = stream.map(prod -> {
			if (prod.getExpirationDate().isBefore(LocalDate.now())) {
				checkOffer(prod);
			}
			return prod;
		})
				.filter(prod -> prod.getExpirationDate().isAfter(LocalDate.now()))
				.filter(prod -> prod.getExpirationDate().isBefore(expiration));

		if (shuffle != null && shuffle) {
			stream = stream.sorted((x1, x2) -> (int) (Math.random() * 10 - 5));
		} else {
			stream = stream.sorted((x1, x2) -> x1.getExpirationDate().compareTo(x2.getExpirationDate()));
		}

		if (offset == null) {
			offset = 0;
		}

		List<Object> ret = new ArrayList<>();
		List<Product> data = new ArrayList<>();
		stream.forEach(data::add); //cause somehow it's throwing exception here

		ret.add(data.size());
		if (limit == null) {
			limit = data.size() - offset;
		}
		ret.addAll(data.subList(offset, (offset + limit > data.size()) ? data.size() : offset + limit));

		return ResponseEntity.ok(ret);
	}

	private boolean checkOffer(Product prod) {
		if (prod.getExpirationDate().isBefore(LocalDate.now())) {
			prod.setDiscount(0.0);
			return true;
		} else {
			return false;
		}
	}

	@GetMapping("/checkOffer")
	public ResponseEntity<String> checkOffer(@RequestParam("prodId") String prodId) {
		checkOffer(prodRepo.findOne(prodId));
		return ResponseEntity.ok((checkOffer(prodRepo.findOne(prodId))) ? "Offer checked: removed" : "Offer is OK");
	}

	@RequestMapping("/list/{group}/{city}")
	public ResponseEntity<java.util.List<Product>> listInCity(@PathVariable("city") @NotNull String cityName, @PathVariable("group") String group) {
		return ResponseEntity.ok(prodRepo.listProductsInCity(cityName, group));
	}


	@RequestMapping("/create")
	public ResponseEntity<Product> createProduct(@RequestParam("title") String title, @RequestParam("genre") String genre,
	                                             @RequestParam("cookie") String cookie, @RequestParam("price") int price,
	                                             @RequestParam("city") String city,
	                                             @RequestParam(value = "img", required = false) String img) {
		val worker = authController.getLoggedToken(cookie, Worker.class);
		val product = new Product(title, gh.getGenre(genre), price, worker, city, img);
		if (img != null) {
			product.setImage(img);
		}
		worker.addProduct(product);
		prodRepo.save(product);
		workerRepo.delete(worker.getId());
		workerRepo.save(worker);
		return ResponseEntity.ok().body(product);
	}
}
