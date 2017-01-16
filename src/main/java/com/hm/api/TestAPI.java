package com.hm.api;

import com.google.gson.Gson;
import com.hm.AppLoader;
import com.hm.entity.*;
import com.hm.model.AuthController;
import com.hm.repo.*;
import com.mongodb.Block;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.hm.manualdb.ConnectionHandler.db;
import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.stream;

@RestController
@RequestMapping("/test")
public class TestAPI {

	@Autowired
	ModeratorRepository moderatorRepo;
	@Autowired
	WorkerRepository workerRepo;
	@Autowired
	ClientRepository clientRepo;
	@Autowired
	GenresHolder gh;
	@Autowired
	private AuthAPI authapi;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AuthRepository authRepo;
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private AuthController authController;
	@Autowired
	private Gson gson;

	@RequestMapping("/user")
	public String testUser() {
		return db().getCollection("users").find(eq("mail", "test1@gg.gg")).first().toJson();
	}

	@RequestMapping("/wipe/db")
	public ResponseEntity wipeDb() {

		return ResponseEntity.ok().build();
	}

	@RequestMapping("/rebuild/db")
	public ResponseEntity rebuildDB() {
		System.out.print("Wiping prev DB... ");
		db().listCollections().forEach((Block<? super Document>) col -> {
			col.entrySet().forEach(ent -> {
				try {
					db().getCollection(ent.getValue().toString()).deleteMany(new Document());
				} catch (Exception e) {
				}
			});
		});

		System.out.println("done.");
		System.out.print("Generating users... ");


		authapi.register("admin@corp.com", "pass", "Moderator", "common/auth" + new Random().nextInt(8) + ".jpg");
		authapi.register("anna@hm.com", "12345", "Moderator", "common/auth" + new Random().nextInt(8) + ".jpg");
		authapi.register("john@doe.com", "12345", "Moderator", "common/auth" + new Random().nextInt(8) + ".jpg");
		authapi.register("dave@doe.com", "12345", "Moderator", "common/auth" + new Random().nextInt(8) + ".jpg");
		authapi.register("moderator@corp.com", "12345", "Moderator", "common/auth" + new Random().nextInt(8) + ".jpg");

//		db().getCollection("user").updateOne(new Document(),
//				new Document("$set", new Document("_class", "com.hm.entity.Moderator"))
//						.append("$set", new Document("accessLevel", 100)));

		Set<User> users = new HashSet<>();

		Set<Worker> workers = new HashSet<>();

		IntStream.range(0, 20).forEach(i -> {
			users.add(authapi.register("worker" + i + "@hm.com", "pass" + i, "Worker", "common/auth" + new Random().nextInt(8) + ".jpg"));
		});

		authapi.register("newuser@mail.com", "12345", "Client", "common/auth" + new Random().nextInt(8) + ".jpg");

		System.out.println("done.");
		System.out.print("Generating products... ");

		Genre genr[] = {gh.createGenre("Свадебный фотограф", "Фотограф", "Популярные"),
				gh.createGenre("Фотосессия", "Фотограф", "Популярные"),
				gh.createGenre("Мастер-шеф", "Кулинар", "Популярные"),
				gh.createGenre("Свадебный торт", "Кулинар", "Популярные"),
				gh.createGenre("Дирижабль", "Аренда транспорта", "Аренда")};

		users.forEach(user -> {
			workers.add(authController.getEntity(user.getId(), Worker.class));
		});

		Set<Product> products = new HashSet<>();

		workers.forEach(worker -> {
			ProductAPI p = (ProductAPI) AppLoader.ctx.getBean("productAPI");
			AuthToken authToken = (AuthToken) authapi.auth(worker.getMail(), worker.getPass()).getBody();
			products.add((Product) p.createProduct("work" + worker.getPass(), genr[(int) (Math.random() * 5)].getName(), authToken.getSessionID(), 1000, "common/auth" + new Random().nextInt(8) + ".jpg").getBody());
		});

		products.stream().filter(e -> Math.random() > 0.5).forEach(product -> {
			product.setDiscount(0.4);
			prodRepo.save(product);
		});


		System.out.println("done.");

		return getAll();
	}

	@RequestMapping("getAll")
	public ResponseEntity getAll() {
		List<String> list = new ArrayList<>();
		userRepo.findAll().forEach(e -> list.add(e.toString()));
		moderatorRepo.findAll().forEach(e -> list.add(e.toString()));
		clientRepo.findAll().forEach(e -> list.add(e.toString()));
		workerRepo.findAll().forEach(e -> list.add(e.toString()));
		authRepo.findAll().forEach(e -> list.add(e.toString()));
		prodRepo.findAll().forEach(e -> list.add(e.toString()));
		gh.getCategories().forEach(e -> {
			list.add(e.toString());
		});

		return ResponseEntity.ok(list);
	}

	@RequestMapping("/getMappings")
	public ResponseEntity getMappings() {
		return ResponseEntity.ok(
				Stream.of(AuthAPI.class, FileAPI.class, ProductAPI.class, TestAPI.class, UserAPI.class)
						.flatMap(clz -> stream(clz.getMethods()))
						.filter(e -> e.getAnnotations().length > 0)
						.filter(e -> stream(e.getAnnotations())
								.map(x -> x.annotationType().getSimpleName())
								.filter(x -> x.equals("RequestMapping") || x.equals("GetMapping"))
								.findAny().isPresent())
						.map(meth -> stream(meth.getDeclaringClass().getAnnotations())
								.filter(x -> x.annotationType().getSimpleName().equals("RequestMapping"))
								.map(x -> (RequestMapping) x)
								.map(RequestMapping::value)
								.map(Arrays::toString)
								.findAny()
								.orElse("!@#$%^&*()")
								+ "/"
								+ meth.getName()
								+ " :: "
								+ stream(meth.getParameterAnnotations())
								.flatMap(Arrays::stream)
								.filter(x -> x.annotationType().getSimpleName().equals("RequestParam"))
								.map(x -> (RequestParam) x)
								.map(RequestParam::value)
								.reduce((s1, s2) -> s1 + ", " + s2)
								.orElseGet(() -> stream(meth.getParameterAnnotations())
										.flatMap(Arrays::stream)
										.filter(x -> x.annotationType().getSimpleName().equals("PathVariable"))
										.map(x -> (PathVariable) x)
										.map(x -> "/" + x.value())
										.reduce((s1, s2) -> s1 + s2)
										.orElse("-----")
								))
						.collect(Collectors.toList()));
	}


	//SHIT BELOW


	@RequestMapping("/env")
	public String sysEnv() {
		return System.getenv("MONGODB_URI");
	}

	@RequestMapping("/test")
	public ResponseEntity testdb() {
		AuthController a = (AuthController) AppLoader.ctx.getBean("authController");
		AuthToken token = a.auth("admin@corp.com", "pass");
		System.out.println(token.toString());
		((AuthController) AppLoader.ctx.getBean("authController")).getEntity("", Moderator.class);
		return ResponseEntity.ok().body("ok");
	}

}
