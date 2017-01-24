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
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.hm.manualdb.ConnectionHandler.db;
import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.stream;

@RestController
@CrossOrigin
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


		authapi.register("admin@corp.com", "pass", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg");
		authapi.register("anna@hm.com", "12345", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg");
		authapi.register("john@doe.com", "12345", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg");
		authapi.register("dave@doe.com", "12345", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg");
		authapi.register("moderator@corp.com", "12345", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg");

		Set<User> users = new HashSet<>();

		Set<Worker> workers = new HashSet<>();

		IntStream.range(0, 100).parallel().forEach(i -> {
			users.add(authapi.register("worker" + i + "@hm.com", "pass" + i, "Worker", "common/auth" + new Random().nextInt(13) + ".jpg"));
		});

		authapi.register("newuser@mail.com", "12345", "Client", "common/auth" + new Random().nextInt(13) + ".jpg");

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

		workers.parallelStream().forEach(worker -> {
			ProductAPI p = (ProductAPI) AppLoader.ctx.getBean(ProductAPI.class);
			AuthToken authToken = (AuthToken) authapi.auth(worker.getMail(), worker.getPass()).getBody();
			products.add(
					p.createProduct("Test work name, hello world!",
							genr[(int) (Math.random() * 5)].getName(),
							authToken.getSessionID(),
							1000,
							ConfigAPI.listCities()[(int) (Math.random() * 3)],
							"common/auth" + new Random().nextInt(13) + ".jpg").getBody());
		});

		products.stream().filter(e -> Math.random() > 0.5).forEach(product -> {
			double disc = Math.random() * 90.0 + 5;
			disc = Math.round(disc);
			product.setDiscount(disc / 100);
			product.setExpirationDate(LocalDate.of(2017, 2, (int) (Math.random() * 28 + 1)));
			prodRepo.save(product);
		});


		System.out.println("done.");

		return getAll();
	}

	@RequestMapping("/update/genres")
	public ResponseEntity updateGenres() {
		return ResponseEntity.ok(AppLoader.ctx.getBean(GenresHolder.class).updateCollectionsDB());
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

	@RequestMapping("getAllObj")
	public ResponseEntity getAllObj() {
		List<Object> list = new ArrayList<>();
		userRepo.findAll().forEach(list::add);
		moderatorRepo.findAll().forEach(list::add);
		clientRepo.findAll().forEach(list::add);
		workerRepo.findAll().forEach(list::add);
		authRepo.findAll().forEach(list::add);
		prodRepo.findAll().forEach(list::add);
		GenresHolder.getCategories().forEach(list::add);
		return ResponseEntity.ok(list);
	}

	@RequestMapping("/getMappings")
	public ResponseEntity getMappings() {
		return ResponseEntity.ok(
				Stream.of(AuthAPI.class, BidsAPI.class, ConfigAPI.class, FileAPI.class, ProductAPI.class, TestAPI.class, UserAPI.class)
						.flatMap(clz -> stream(clz.getMethods()))
						.filter(e -> e.getAnnotations().length > 0)
						.filter((Method e) -> stream(e.getAnnotations())
								.map(x -> x.annotationType().getSimpleName())
								.filter(x -> x.equals("RequestMapping") || x.equals("GetMapping"))
								.findAny().isPresent())
						.map((Method meth) -> stream(meth.getDeclaringClass().getAnnotations())
								.filter(x -> x.annotationType().getSimpleName().equals("RequestMapping"))
								.map(x -> (RequestMapping) x)
								.map(RequestMapping::value)
								.map(arr -> (arr.length == 1) ? arr[0] : Arrays.toString(arr))
								.findAny()
								.orElse("Nothing")
								+
								((meth.getAnnotation(RequestMapping.class) != null && meth.getAnnotation(RequestMapping.class).value().length == 1)
										? meth.getAnnotation(RequestMapping.class).value()[0]
										: ((meth.getAnnotation(GetMapping.class) != null)
										? ((meth.getAnnotation(GetMapping.class).value().length == 1)
										? meth.getAnnotation(GetMapping.class).value()[0]
										: Arrays.toString(meth.getAnnotation(GetMapping.class).value()))
										: Arrays.toString(meth.getAnnotation(RequestMapping.class).value()))
								)
								+ " :: "
								+ stream(meth.getParameterAnnotations())
								.flatMap(Arrays::stream)
								.filter(x -> x.annotationType().getSimpleName().equals("RequestParam"))
								.map(x -> (RequestParam) x)
								.map(RequestParam::value)
								.reduce((s1, s2) -> s1 + ", " + s2)
								.orElse("-----"))
						.collect(Collectors.toList()));
	}


	private static class NameGen {
		public static ArrayList<Character> glasn = new ArrayList<>();

		static {
			char[] characters = {'a', 'i', 'u', 'e', 'o', 'y'};
			for (char char_ : characters) {
				glasn.add(char_);
			}
		}

		public static String[] genNames(int size) {
			String[] names = new String[size];
			for (int i = 0; i < size; i++) {
				names[i] = genName(4);
			}
			return names;
		}

		public static String genName(int minLength) {
			StringBuilder sb;
			double melodical = 0.33;
			double k = melodical;
			double kvar = 1.33;
			Random r = new Random();
			char temp;
			boolean glasnoe;
			sb = new StringBuilder();
			for (int i = 0; i < (Math.random() * minLength) + 3; i++) {
				glasnoe = (Math.random() > k);
				do {
					temp = (char) (r.nextInt(('Z' - 'A' + 1)) + 'a');
				} while (checkGlasn(glasnoe, temp));
				k = ((glasnoe) ? k * kvar : k / kvar);
				if (i == 0) {
					temp = Character.toUpperCase(temp);
				}
				sb.append(temp);
			}
			return sb.toString();
		}

		private static boolean checkGlasn(boolean cond, char temp) {
			if (glasn.contains(temp) == cond) {
				return true;
			}
			return false;
		}
	}
}
