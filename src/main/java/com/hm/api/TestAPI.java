package com.hm.api;

import com.google.gson.Gson;
import com.hm.AppLoader;
import com.hm.api.admin.DevAdminAPI;
import com.hm.api.admin.UserAdminAPI;
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
	private UserAdminAPI userAdminAPI;
	@Autowired
	private ProductAPI productAPI;
	@Autowired
	private ConfigAPI configAPI;
	@Autowired
	private NewsAPI newsAPI;
	@Autowired
	private UserAPI userAPI;
	@Autowired
	private TendersAPI tendersAPI;
	@Autowired
	private Gson gson;

	@RequestMapping("/rebuild/db")
	public ResponseEntity rebuildDB() {
		System.out.print("Wiping prev DB... ");
		db().listCollections().forEach((Block<? super Document>) col -> {
			col.entrySet().forEach(ent -> {
				try {
					db().getCollection(ent.getValue().toString()).deleteMany(new Document());
				} catch (Exception ignored) {
				}
			});
		});

		System.out.println("done.");

		configAPI.cityDefaults();

		System.out.print("Generating users... ");

		authapi.register("admin@corp.com", "pass", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg", "Киев", NameGen.genName(5) + " " + NameGen.genName(5));
		authapi.register("anna@hm.com", "12345", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg", "Киев", "Anna Mart");
		authapi.register("john@doe.com", "12345", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg", "Киев", NameGen.genName(5) + " " + NameGen.genName(5));
		authapi.register("dave@doe.com", "12345", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg", "Киев", NameGen.genName(5) + " " + NameGen.genName(5));
		authapi.register("moderator@corp.com", "12345", "Moderator", "common/auth" + new Random().nextInt(5) + 8 + ".jpg", "Киев", NameGen.genName(5) + " " + NameGen.genName(5));

		Set<User> workingusers = new HashSet<>();
		Set<Worker> workers = new HashSet<>();

		generateWorkers(workingusers);

		List<AuthToken> clientsTokens = new LinkedList<>();
		IntStream.range(0, 100).parallel().forEach(i -> {
			User u = authapi.register("newuser" + i + "@mail.com", "12345", "client",
					"common/auth" + new Random().nextInt(13) + ".jpg", configAPI.listCities()[(int) (Math.random() * 3)],
					NameGen.genName(5) + " " + NameGen.genName(5));
			clientsTokens.add((AuthToken) authapi.auth(u.getMail(), u.getPass()).getBody());
		});

		System.out.println("done.");
		System.out.print("Generating productsIDs... ");

		gh.TEST(); // removes cached data in categories to reimport

		Genre genr[] = generateGenres();

		workingusers.forEach(user -> {
			workers.add(authController.getEntity(user.getId(), Worker.class));
		});


		Set<Product> products = generateProducts(workingusers, genr);


		System.out.println("done. \nGenerating news");

		generateNews();

		//TENDERS HERE

		IntStream.range(0, 20).parallel().forEach(i -> {
			tendersAPI.createTender(new String[]{"genre=Фотосессия", "title=test" + i, "city=" + configAPI.listCities()[(int) (Math.random() * 3)],
					"deadline=2017-02-" + (i % 18 + 10), "price=" + (500 + new Random().nextInt(1000)),
					"workingHours=" + new Random().nextInt(12), "token=" + clientsTokens.get(i).getSessionID()}, "TEST ZAKAZ");

		});


		//TESTING PHOTOGRAPHERS HERE

		generatePhotographers(genr);


		return getAll();
	}

	private Set<Product> generateProducts(Set<User> workingusers, Genre[] genr) {
		Set<Product> products = new HashSet<>();

		workingusers.parallelStream().forEach(worker -> {
			AuthToken authToken = (AuthToken) authapi.auth(worker.getMail(), worker.getPass()).getBody();
			products.add(
					productAPI.createProduct("Test work name, hello world!",
							genr[(int) (Math.random() * 5)].getName(),
							authToken.getSessionID(),
							1000,
							configAPI.listCities()[(int) (Math.random() * 3)],
							"common/auth" + new Random().nextInt(13) + ".jpg").getBody());
			authapi.updateDescription(NameGen.genText(40), authToken.getSessionID());
		});

		products.stream().filter(e -> Math.random() > 0.5).forEach(product -> {
			double disc = Math.random() * 90.0 + 5;
			disc = Math.round(disc);
			product.setDiscount(disc / 100);
			product.setExpirationDate(LocalDate.of(2017, 2, (int) (Math.random() * 28 + 1)));
		});

		products.parallelStream().forEach(prod -> {
			prod.setDescription(Arrays.toString(NameGen.genNames(50)).toLowerCase().substring(1).replace(']', '!'));
			prodRepo.save(prod);
		});
		return products;

	}

	private Genre[] generateGenres() {
		return new Genre[]{
				gh.createGenre("Мастер-шеф", "Кулинар", "Популярные"),
				gh.createGenre("Свадебный торт", "Кулинар", "Популярные"),
				gh.createGenre("Дирижабль", "Аренда транспорта", "Аренда"),
				gh.createGenre("Свадебный фотограф", "Фотограф", "Популярные"),
				gh.createGenre("Фотосессия", "Фотограф", "Популярные"),
				gh.createGenre("Студийная съёмка", "Фотограф", "Популярные"),
				gh.createGenre("Венчание", "Фотограф", "Популярные"),
				gh.createGenre("Детская", "Фотограф", "Популярные"),
				gh.createGenre("Семейная", "Фотограф", "Популярные")
		};
	}

	private void generatePhotographers(Genre[] genr) {
		IntStream.range(0, 100).parallel().forEach(i -> {
			authapi.register("photo" + i + "@test.com", "pass", "Worker", "common/auth" + new Random().nextInt(13) + ".jpg",
					configAPI.listCities()[(int) (Math.random() * 3)], NameGen.genName(5) + " " + NameGen.genName(5));
			AuthToken authToken = (AuthToken) authapi.auth("photo" + i + "@test.com", "pass").getBody();
			// 6 + 3
			productAPI.createProduct("test of " + authToken.getUser().getName(),
					genr[new Random().nextInt(6) + 3].getName(),
					authToken.getSessionID(),
					new Random().nextInt(1000) + 500,
					configAPI.listCities()[(int) (Math.random() * 3)],
					"common/auth" + new Random().nextInt(13) + ".jpg"
			);
			authapi.updateDescription(NameGen.genText(40), authToken.getSessionID());
		});
	}

	private void generateWorkers(Set<User> workingusers) {
		IntStream.range(0, 100).parallel().forEach(i -> {
			User w = authapi.register("worker" + i + "@hm.com", "pass" + i, "Worker",
					"common/auth" + new Random().nextInt(13) + ".jpg",
					configAPI.listCities()[(int) (Math.random() * 3)], NameGen.genName(5) + " " + NameGen.genName(5));
			workingusers.add(w);
			if (Math.random() > 0.5) {
				userAdminAPI.workerPromoteToPro(w.getId(), "here is some auth token, lol"); //TODO add real token here
			}
		});
	}

	private void generateNews() {
		IntStream.range(0, 20).parallel().forEach(i -> {
			Random r = new Random();
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < 5_000; x++) {
				sb.append((char) (r.nextInt(26) + 'a'));
				if (Math.random() > 0.8) {
					sb.append(" ");
				}
			}
			newsAPI.postNews(sb.subSequence(0, 20).toString(), sb.toString(), "", "common/auth" + new Random().nextInt(13) + ".jpg");
		});
	}


	// ----------------------------------------------------
	// ---------------------- E N D -----------------------
	// --------------- of generator scripts ---------------
	// ----------------------------------------------------


	@RequestMapping("/update/genres")
	public ResponseEntity updateGenres() {
		return ResponseEntity.ok(AppLoader.ctx.getBean(GenresHolder.class).updateCollectionsDB());
	}

	@RequestMapping("getAll")
	public ResponseEntity getAll() {
		return ResponseEntity.ok(getAllObj().stream().map(Object::toString).collect(Collectors.toList()));
	}

	@RequestMapping("getAllObj")
	public List<Object> getAllObj() {
		List<Object> list = new ArrayList<>();
		userRepo.findAll().forEach(list::add);
		moderatorRepo.findAll().forEach(list::add);
		clientRepo.findAll().forEach(list::add);
		workerRepo.findAll().forEach(list::add);
		authRepo.findAll().forEach(list::add);
		prodRepo.findAll().forEach(list::add);
		GenresHolder.getCategories().forEach(list::add);
		return list;
	}

	@RequestMapping("/testLogs")
	public ResponseEntity testLogs() {
		throw new NullPointerException("This is TEST HEROKU");
	}

	@RequestMapping("/getMappings")
	public ResponseEntity getMappings() {
		return ResponseEntity.ok(
				Stream.of(AuthAPI.class, TendersAPI.class, ConfigAPI.class, FileAPI.class, FreePhotoAPI.class,
						ProductAPI.class, TestAPI.class, UserAPI.class, UserAdminAPI.class, DevAdminAPI.class)
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

		public static String genText(int length) {
			String[] text = genNames(length);
			StringBuilder sb = new StringBuilder();
			Arrays.stream(text).forEach(sb::append);
			return sb.toString();
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
