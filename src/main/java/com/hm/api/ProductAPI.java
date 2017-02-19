package com.hm.api;

import com.hm.AppLoader;
import com.hm.entity.*;
import com.hm.interceptor.Auth;
import com.hm.model.AuthController;
import com.hm.repo.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@RestController
@CrossOrigin
@RequestMapping("/api/product")
public class ProductAPI {

	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private GenresHolder gh;
	@Autowired
	private AuthController authController;
	@Autowired
	private WorkerRepository workerRepo;
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private ConfigAPI configAPI;

	@GetMapping("/{id}")
	public Product getProduct(@PathVariable("id") String id) {
		return prodRepo.findOne(id);
	}

	@RequestMapping("/categories")
	public ResponseEntity<java.util.Collection<com.hm.entity.Category>> getHat() {
		return ResponseEntity.ok(GenresHolder.getCategories());
	}

	/**
	 * List products with discounts
	 *
	 * @param date   - up to this date offers shown
	 * @param offset - objects to skip
	 */
	@RequestMapping("/offer")
	public ResponseEntity listProductsWithOffer(@RequestParam(value = "city", required = false) String city,
	                                            @RequestParam(value = "shuffle", required = false) Boolean shuffle,
	                                            @RequestParam(value = "genre", required = false) String genre,
	                                            @RequestParam(value = "group", required = false) String group,
	                                            @RequestParam(value = "limit", required = false) Integer limit,
	                                            @RequestParam(value = "date", required = false) String date,
	                                            @RequestParam(value = "offset", required = false) Integer offset) throws Exception {
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

		args.put("isOfferValid", true);

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

		stream = stream.map(prod -> {
			if (prod.getExpirationDate().isBefore(LocalDate.now())) {
				checkOffer(prod);
			}
			return prod;
		})
				.filter(prod -> prod.getExpirationDate().isAfter(LocalDate.now()))
				.filter(prod -> prod.getExpirationDate().isBefore(expiration.plusDays(1)));

		if (offset == null) {
			offset = 0;
		}

		stream = stream.sorted(Comparator.comparing(Product::getExpirationDate))
				.skip(offset);

		List<Object> ret = new ArrayList<>();
		List<Product> data = new ArrayList<>();

		stream.forEach(data::add);

		if (shuffle != null && shuffle && limit != null) {
			ret.add(limit);
			for (int i = 0; i < limit; i++) {
				ret.add(data.get(new Random().nextInt(data.size())));
			}
			return ResponseEntity.ok(ret);
		}

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

	@GetMapping("/getUserProducts/{userid}")
	public ResponseEntity<List<Product>> getUserProducts(@PathVariable("userid") String userid) {
		return ResponseEntity.ok(prodRepo.listByWorkerId(userid));
	}

	@GetMapping("/getUserProducts/arr/{userid}")
	public ResponseEntity getUserProductsArrays(@PathVariable("userid") String userid) {
		HashMap<String, List<Product>> ret = new HashMap<>();
		prodRepo.listByWorkerId(userid).stream().forEach(prod -> {
			ret.putIfAbsent(prod.getGenreName(), new ArrayList<>());
			ret.get(prod.getGenreName()).add(prod);
		});
		return ResponseEntity.ok(ret.values());
	}

	@RequestMapping("/list/{group}/{city}")
	public ResponseEntity listInCity(@PathVariable("city") @NotNull String cityName, @PathVariable("group") String group) {
		return ResponseEntity.ok(prodRepo.listProductsInCity(cityName, group));
	}

	@GetMapping("/buy/{productId}")
	public ResponseEntity buy(@PathVariable("productId") String productid, @RequestParam("token") String token,
	                          @RequestParam(value = "hours", required = false) Integer hours) {
		Product product = prodRepo.findOne(productid);
		User user = authController.getUser(token);
		if (product == null || user == null) {
			return ResponseEntity.status(403).body("Wrong data input");
		}
		if (hours == null) {
			hours = 1;
		}
		Order order = new Order(user, product, hours);
		orderRepo.save(order);
		return ResponseEntity.ok(null);
	}


	@Auth("worker")
	@RequestMapping("/create")
	public ResponseEntity<Product> createProduct(@RequestParam("title") String title, @RequestParam("genre") String genre,
	                                             @RequestParam("cookie") String cookie, @RequestParam("price") int price,
	                                             @RequestParam("city") String city,
	                                             @RequestParam(value = "img", required = false) String img) {
		val worker = authController.getLoggedToken(cookie, Worker.class);
		if (worker == null) {
			return ResponseEntity.status(403).build();
		}
		val product = new Product(title, gh.getGenre(genre), price, worker, city, img);
		product.setId(ConfigAPI.getNextProdId() + "");
		if (img != null) {
			product.setImage(img);
		}
		worker.addProduct(product);
		prodRepo.save(product);
		workerRepo.delete(worker.getId());
		workerRepo.save(worker);
		return ResponseEntity.ok().body(product);
	}


	@GetMapping("/attachPage")
	public ResponseEntity attachPage(@RequestParam("productid") String prodid,
	                                 @RequestParam("pageid") String pageid) {
		Product prod = prodRepo.findOne(prodid);
		LinkedPage page = AppLoader.ctx.getBean(PagesRepository.class).findOne(pageid);
		if (prod == null) {
			return ResponseEntity.status(403).body("Wrong ID");
		}
		prod.setLinkedPageId(page.getId());
		prodRepo.save(prod);
		return ResponseEntity.ok(prod);
	}

	@GetMapping("/addphoto/{id}")
	public ResponseEntity<Product> addPhoto(@RequestParam("path") String path,
	                                        @PathVariable("id") String id,
	                                        @RequestParam("token") String token) {
		Product p = prodRepo.findOne(id);
		p.addPhoto(path);
		prodRepo.save(p);
		return ResponseEntity.ok(p);
	}
}
