package com.hm.api;

import com.google.gson.Gson;
import com.hm.AppLoader;
import com.hm.entity.Genre;
import com.hm.entity.Product;
import com.hm.repo.GenresHolder;
import com.hm.repo.ProductRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hm.manualdb.ConnectionHandler.db;

@RestController
@CrossOrigin
@RequestMapping("/api/config")
public class ConfigAPI {

	private static String[] cityCache;
	private static Integer nextUserId;
	private static Integer nextTenderId;
	private static Integer nextProductId;

	static {
		updateNexts();
	}

	@Autowired
	Gson gson;

	public static void updateNexts() {
		try {
			Object userVal = db().getCollection("config")
					.find(Document.parse("{'key' : 'nextUserId'}")).first().get("value");
			Object tendVal = db().getCollection("config")
					.find(Document.parse("{'key' : 'nextTenderId'}")).first().get("value");
			Object prodVal = db().getCollection("config")
					.find(Document.parse("{'key' : 'nextProductId'}")).first().get("value");
			if (userVal instanceof Integer) {
				nextUserId = (Integer) userVal;
				nextTenderId = (Integer) tendVal;
				nextProductId = (Integer) prodVal;
			} else if (userVal instanceof Double) {
				nextUserId = Math.toIntExact(Math.round((Double) userVal));
				nextTenderId = Math.toIntExact(Math.round((Double) tendVal));
				nextProductId = Math.toIntExact(Math.round((Double) prodVal));
			} else {
				throw new UnsupportedClassVersionError("Expected double or int");
			}
		} catch (Exception e) {
			defaults();
		}
	}

	public static String[] defaults() { //TODO Delete this!
		cityCache = null; //will be un-nulled in listCities
		db().getCollection("config").drop();
		nextUserId = 0;
		nextTenderId = 0;
		nextProductId = 0;
		db().getCollection("config").insertOne(Document.parse("{'key' : 'nextUserId', 'value': 0}"));
		db().getCollection("config").insertOne(Document.parse("{'key' : 'nextTenderId', 'value': 0}"));
		db().getCollection("config").insertOne(Document.parse("{'key' : 'nextProductId', 'value': 0}"));
		db().getCollection("config").insertOne(Document.parse("{'key':'cities', 'value':['Киев', 'Одесса', 'Львов']}"));
		return listCities();
	}

	/**
	 * Lists cities to APIs and inner methods.
	 * @return Cities in db/app
	 */
	@GetMapping("/list/city")
	public static String[] listCities() {
		if (cityCache == null || cityCache.length < 3) {
			try {
				List<String> list = (List<String>) ((ArrayList) db().getCollection("config")
						.find(Document.parse("{'key':'cities'}"))
						.first()
						.get("value")
				)
						.stream()
						.map(Object::toString)
						.collect(Collectors.toList());
				cityCache = list.toArray(new String[0]);
			} catch (Exception e) { //just another algorythm
				String get = db().getCollection("config").find(Document.parse("{'key':'cities'}")).first().get("value").toString();
				get = get.substring(1, get.length() - 1);
				String[] cities = get.split(",");
				for (int i = 0; i < cities.length; i++) {
					cities[i] = cities[i].trim();
				}
			}
		}
		return cityCache;
	}

	@GetMapping("/getNextUserId")
	public static synchronized int getNextUserId() {
		new Thread(() -> {
			db().getCollection("config").updateOne(Document.parse("{'key' : 'nextUserId'}"),
					Document.parse("{$inc : {'value' : 1}}"));
		}).start();
		return nextUserId++;
	}

	@GetMapping("/getNextProdId")
	public static synchronized int getNextProdId() {
		new Thread(() -> {
			db().getCollection("config").updateOne(Document.parse("{'key' : 'nextProductId'}"),
					Document.parse("{$inc : {'value' : 1}}"));
		}).start();
		return nextProductId++;
	}

	@GetMapping("/getNextTenderId")
	public static synchronized int getNextTenderId() {
		new Thread(() -> {
			db().getCollection("config").updateOne(Document.parse("{'key' : 'nextTenderId'}"),
					Document.parse("{$inc : {'value' : 1}}"));
		}).start();
		return nextTenderId++;
	}

	@GetMapping("/list/city/add/{name}")
	public ResponseEntity add(@PathVariable("name") String name) { //TODO Add token and move to admin API
		db().getCollection("config").updateOne(Document.parse("{'key':'cities'}"),
				Document.parse("{$push : {'value' : '" + name + "'}}"));
		String[] tmp = Arrays.copyOf(cityCache, cityCache.length+1);
		tmp[tmp.length-1] = name;
		cityCache = tmp;
		return ResponseEntity.ok(cityCache);
	}

	@GetMapping("/list/genre")
	public ResponseEntity listGenres() {
		return ResponseEntity.ok(GenresHolder.getCategories());
	}

	@GetMapping("/list/offer/group")
	public ResponseEntity listOfferGroups() {
		return ResponseEntity.ok(AppLoader.ctx.getBean(ProductRepository.class)
				.listCustom1ArgQuery("offeredPrice", true)
				.map(Product::getGroupName)
				.distinct()
				.collect(Collectors.toList())
		);
	}

	@GetMapping("/list/offer/genre/{group}")
	public ResponseEntity listOfferGenres(@PathVariable("group") String group) {
		return ResponseEntity.ok(AppLoader.ctx.getBean(ProductRepository.class)
				.listCustom2ArgQuery("offeredPrice", true, "groupName", group)
				.flatMap(prod -> GenresHolder.getGroup(prod.getGroupName()).getGenres().stream())
				.map(Genre::getName)
				.distinct()
				.collect(Collectors.toList())
		);
	}

}
