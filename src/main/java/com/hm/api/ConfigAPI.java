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
@RequestMapping("/config")
public class ConfigAPI {

	private static String[] cityCache;
	@Autowired
	Gson gson;

	/**
	 * Lists cities to APIs and inner methods.
	 * @return Cities in db/app
	 */
	@GetMapping("/list/city")
	public static String[] listCities() {
		if (cityCache == null || cityCache.length < 3) {
			List<String> list = (List<String>) ((ArrayList) db().getCollection("config")
					.find(Document.parse("{'key':'cities'}"))
					.first()
					.get("value")
			)
					.stream()
					.map(Object::toString)
					.collect(Collectors.toList());
			cityCache = list.toArray(new String[0]);

			/* OR THIS */

//			String get = db().getCollection("config").find(Document.parse("{'key':'cities'}")).first().get("value").toString();
//			get = get.substring(1, get.length()-1);
//			String[] cities = get.split(",");
//			for (int i = 0; i < cities.length; i++) {
//				cities[i] = cities[i].trim();
//			}
		}
		return cityCache;
	}

	@GetMapping("/list/city/defaults")
	public String[] cityDefaults() { //TODO Delete this!
		cityCache = null; //will be un-nulled in listCities
		db().getCollection("config").deleteMany(Document.parse("{'key':'cities'}"));
		db().getCollection("config").insertOne(Document.parse("{'key':'cities', 'value':['Киев', 'Одесса', 'Львов']}"));
		return listCities();
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
