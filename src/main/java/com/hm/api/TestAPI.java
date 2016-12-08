package com.hm.api;

import com.google.gson.Gson;
import com.hm.repo.AuthRepository;
import com.hm.repo.GenresHolder;
import com.hm.repo.ProductRepository;
import com.hm.repo.UserRepository;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.hm.manualdb.ConnectionHandler.db;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;

@RestController
@RequestMapping("/test")
public class TestAPI {

	@Autowired
	private AuthAPI authapi;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AuthRepository authRepo;
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private UserAPI userApi;
	@Autowired
	private Gson gson;

	@Autowired
	GenresHolder gh;

	@RequestMapping("/user")
	public String testUser() {
		return db().getCollection("users").find(eq("mail", "test1@gg.gg")).first().toJson();
	}

	@RequestMapping("/rebuild/db")
	public ResponseEntity rebuildDB() {

		userRepo.deleteAll();
		authRepo.deleteAll();

		authapi.register("admin@corp.com", "pass");
		authapi.register("anna@hm.com", "12345");
		authapi.register("john@doe.com", "12345");
		authapi.register("dave@doe.com", "12345");
		authapi.register("moderator@corp.com", "12345");

//		db().getCollection("user").updateOne(new Document(),
//				new Document("$set", new Document("_class", "com.hm.entity.Moderator"))
//						.append("$set", new Document("accessLevel", 100)));

		userRepo.findAll().forEach(user -> {
			userApi.promoteToEmployee(user.getMail());
		});

		authapi.register("newuser@mail.com", "12345");

		gh.createGenre("Свадебный фотограф", "Фотограф", "Популярные");
		gh.createGenre("Фотосессия", "Фотограф", "Популярные");
		gh.createGenre("Мастер-шеф", "Кулинар", "Популярные");
		gh.createGenre("Свадебный торт", "Кулинар", "Популярные");
		gh.createGenre("Дирижабль", "Аренда транспорта", "Аренда");

		return getAll();
	}

	@RequestMapping("getAll")
	public ResponseEntity getAll() {
		List<String> list = new ArrayList<>();
		userRepo.findAll().forEach(e -> list.add(e.toString()));
		authRepo.findAll().forEach(e -> list.add(e.toString()));
		gh.getCategories().values().forEach(e -> {
			System.out.println(e);
			list.add(e.toString());
		});
		gh.getGroups().values().forEach(e -> {
			System.out.println(e);
			list.add(e.toString());
		});
		gh.getGenres().values().forEach(e -> {
			System.out.println(e);
			list.add(e.toString());
		});
		return ResponseEntity.ok(list);
	}


	//SHIT BELOW


	@RequestMapping("/db3")
	public String testdb3() {
		StringBuilder ret = new StringBuilder();
		db().getCollection("restaurants").find(
				lt("address.zipcode", 10000)
		).forEach((Block<? super Document>) el -> ret.append(el));
		return ret.toString();
	}

	@RequestMapping("/env")
	public String sysEnv() {
		return System.getenv("MONGODB_URI");
	}

	@RequestMapping("/db")
	public String testdb() {
		StringBuilder ret = new StringBuilder();

		db().getCollection("restaurants").insertOne(
				new Document("address",
						new Document()
								.append("street", "2 Avenue")
								.append("zipcode", "10075")
								.append("building", "1480")
								.append("borough", "Manhattan")
								.append("cuisine", "Italian")
								.append("name", "Vella")
								.append("restaurant_id", "41704620")));

		FindIterable<Document> iterable = db().getCollection("restaurants").find();

		iterable.forEach((Block<? super Document>) i -> {
			System.out.println(i);
			ret.append(i + " <br>");
		});

		return ret.toString();
	}

}
