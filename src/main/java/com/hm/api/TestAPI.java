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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static com.hm.manualdb.ConnectionHandler.db;
import static com.mongodb.client.model.Filters.eq;

@RestController
@RequestMapping("/test")
public class TestAPI {

	@Autowired
	private AuthAPI authapi;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	ModeratorRepository moderatorRepo;
	@Autowired
	WorkerRepository workerRepo;
	@Autowired
	ClientRepository clientRepo;
	@Autowired
	private AuthRepository authRepo;
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private AuthController authController;
	@Autowired
	private Gson gson;

	@Autowired
	GenresHolder gh;

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
		db().listCollections().forEach((Block<? super Document>) col -> {
			col.entrySet().forEach(ent -> {
				try {
					db().getCollection(ent.getValue().toString()).deleteMany(new Document());
				}catch (Exception e) {
				}
			});
		});

		authapi.register("admin@corp.com", "pass", "Moderator");
		authapi.register("anna@hm.com", "12345", "Moderator");
		authapi.register("john@doe.com", "12345", "Moderator");
		authapi.register("dave@doe.com", "12345", "Moderator");
		authapi.register("moderator@corp.com", "12345", "Moderator");

//		db().getCollection("user").updateOne(new Document(),
//				new Document("$set", new Document("_class", "com.hm.entity.Moderator"))
//						.append("$set", new Document("accessLevel", 100)));

		Set<User> users = new HashSet<>();

		Set<Worker> workers = new HashSet<>();

		IntStream.range(0, 20).forEach(i -> {
			users.add(authapi.register("worker" + i + "@hm.com", "pass" + i, "Worker"));
		});

		authapi.register("newuser@mail.com", "12345", "Client");

		Genre genr[] = {gh.createGenre("Свадебный фотограф", "Фотограф", "Популярные"),
				gh.createGenre("Фотосессия", "Фотограф", "Популярные"),
				gh.createGenre("Мастер-шеф", "Кулинар", "Популярные"),
				gh.createGenre("Свадебный торт", "Кулинар", "Популярные"),
				gh.createGenre("Дирижабль", "Аренда транспорта", "Аренда")};

		users.forEach(user -> {
			workers.add(authController.getEntity(user.getId(), Worker.class));
		});

		workers.forEach(worker -> {
			ProductAPI p = (ProductAPI) AppLoader.ctx.getBean("productAPI");
			AuthToken authToken = (AuthToken) authapi.auth(worker.getMail(), worker.getPass()).getBody();
			p.createProduct("work"+worker.getPass(), genr[(int) (Math.random()*5)].getName(), authToken.getSessionID());
		});


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
		gh.getCategories().values().forEach(e -> {
			System.out.println(e);
			list.add(e.toString());
		});

		return ResponseEntity.ok(list);
	}


	//SHIT BELOW


	@RequestMapping("/env")
	public String sysEnv() {
		return System.getenv("MONGODB_URI");
	}

	@RequestMapping("/test")
	public ResponseEntity testdb() {
		AuthController a = (AuthController)AppLoader.ctx.getBean("authController");
		AuthToken token = a.auth("admin@corp.com", "pass");
		System.out.println(token.toString());
		((AuthController)AppLoader.ctx.getBean("authController")).getEntity("", Moderator.class);
		return ResponseEntity.ok().body("ok");
	}

}
