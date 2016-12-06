package com.hm.api;

import com.google.gson.Gson;
import com.hm.dao.CollectionNames;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import static com.hm.dao.db.ConnectionHandler.db;
import static com.mongodb.client.model.Filters.*;

@RestController
@RequestMapping("/test")
public class TestAPI {

	@Autowired
	private AuthAPI authapi;
	@Autowired
	private Gson gson;

	@RequestMapping("/user")
	public String testUser () {
		return db().getCollection("users").find(eq("mail", "test1@gg.gg")).first().toJson();
	}

	@RequestMapping("/rebuild/db")
	public String rebuildDB () {

		Arrays.asList(CollectionNames.values()).forEach(e -> {
			db().getCollection(e.toString()).deleteMany(new Document());
		});

		authapi.register("admin@corp.com", "pass");
		authapi.register("anna@hm.com", "12345");
		authapi.register("john@doe.com", "12345");
		authapi.register("dave@doe.com", "12345");
		authapi.register("moderator@corp.com", "12345");

		db().getCollection("users").updateMany(new Document(),
				new Document("$set", new Document("accessLevel", 100)));

		authapi.register("newuser@mail.com", "12345");

		return "hehe";
	}

	@RequestMapping("getAll")
	public String getAll () {
		StringBuilder ret = new StringBuilder();
		for (Object o : CollectionNames.values()) {
			db().getCollection(o.toString()).find().forEach((Block<? super Document>) el -> ret.append(el.toJson() + "<br>"));
		}
		return ret.toString();
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
	public String sysEnv () {
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
