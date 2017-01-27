package com.hm.repo;

import com.google.gson.Gson;
import com.hm.AppLoader;
import com.hm.entity.Category;
import com.hm.entity.Genre;
import com.hm.entity.Group;
import lombok.Data;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;

import static com.hm.manualdb.ConnectionHandler.db;

@Data
@Repository
public class GenresHolder {

	private static final Gson gson = new Gson();
	@Autowired
	private CategoryRepository categoryRepo;
	private static HashMap<String, Category> categories = null;

	public GenresHolder() {
		new Thread(this::updateCollectionsDB).start();
	}

	public static void TEST () { //TODO DELETE IN RELEASE
		categories = new HashMap<>(); //FIXME DELETE IN RELEASE
	}

	public int updateCollectionsDB() {
		categories = null;
		boolean flag = true;
		while (flag) {
			while (AppLoader.ctx == null) {
			} //while app loads
			try {
				if (categories == null) {
					categories = new HashMap<>();
					categoryRepo.findAll()
							.forEach(category -> categories.put(category.getName(), category));
				}
				flag = false;
				System.out.println("GenresHolder init success.");
			} catch (Exception e) {
				System.out.println("Restarting init of GenresHolder...");
				categories = null;
				flag = true;
			}
		}
		return categories.values().size();
	}

	public static Collection<Category> getCategories() {
		return categories.values();
	}

	public static boolean isGenreExists(String s) {
		return categories.values().stream()
				.flatMap(cat -> cat.getGroups().stream())
				.flatMap(group -> group.getGenres().stream())
				.filter(genre -> genre.getName().equals(s))
				.findFirst()
				.orElse(null) != null;
	}

	public void save() {
		categoryRepo.deleteAll();
		categoryRepo.save(categories.values());
	}

	public void addCategory(Category category) {
		categoryRepo.save(category);
		categories.put(category.getName(), category);
	}

	/**
	 * FIXME This is unused method, not optimized. Also it is not tested. You are noticed.
	 */
	public void addGroup(Group group) {
		group.categoryEntity().getGroups().add(group);
		if (categories.get(group.getCategoryName()) == null) {
			addCategory(group.categoryEntity());
			save();
		} else {
			db().getCollection("category")
					.updateOne(Document.parse("{'name' : '" + group.getCategoryName() + "'}"),
							Document.parse("{$set: {'groups':" + gson.toJson(group) + "}}")); //FIXME test this query
		}
	}

	/**
	 FIXME This is unused method, not optimized. If U trying to use it - refactor and update it first
	 */
	public void addGenre(Genre genre) {
		Category category;
		if (categories.get(genre.getCategoryName()) == null) {
			category = createCategory(genre.getCategoryName());
		} else {
			category = getCategory(genre.getCategoryName());
		}

		if (getGroup(genre.getGroupName()) == null) {
			createGroup(genre.getGroupName(), category);
		}

		categories.get(genre.getCategoryName()).getGroups().stream()
				.filter(grp -> grp.getName().equals(genre.getGroupName()))
				.findFirst()
				.ifPresent(tar -> tar.getGenres().add(genre));

		save();
	}

	private Group createGroup(String groupName, Category category) {
		return category.getGroups().stream()
				.filter(group -> group.getName().equals(groupName))
				.findFirst()
				.orElseGet(() -> {
					Group ret = new Group(groupName, category);
					category.getGroups().add(ret);
					save();
					return ret;
				});
	}

	private Category createCategory(String categoryName) {
		Category category;
		if (categories.get(categoryName) == null) {
			category = new Category(categoryName);
			categories.put(categoryName, category);
			addCategory(category);
		} else {
			category = categories.get(categoryName);
		}
		return category;
	}

	public Genre createGenre(String genreName, String groupName, String categoryName) {
		Category category = createCategory(categoryName);
		Group group = createGroup(groupName, category);
		return group.getGenres().stream()
				.filter(g -> g.getName().equals(genreName))
				.findFirst()
				.orElseGet(() -> {
					Genre genre = new Genre(genreName, group);
					group.getGenres().add(genre);
					save();
					return genre;
				});
	}

	public static Group getGroup(String groupName) {
		return categories.values().stream()
				.flatMap(cat -> cat.getGroups().stream())
				.filter(group -> group.getName().equals(groupName) || group.getId().equals(groupName))
				.findFirst()
				.orElse(null);
	}

	public static Genre getGenre(String genreName) {
		return categories.values().parallelStream()
				.flatMap(category -> category.getGroups()
						.parallelStream())
				.flatMap(group -> group.getGenres()
						.parallelStream())
				.filter(genre -> genre.getName().equals(genreName))
				.findFirst()
				.orElse(null);
	}

	public static Category getCategory(String categoryId) {
		return categories.values()
				.stream()
				.filter(e -> e.getId().equals(categoryId))
				.findFirst()
				.orElse(null);
	}


	public boolean deleteGenre(String genreName) {
		Genre genre = getGenre(genreName);
		Group group = getGroup(genre.getGroupName());
		return group.getGenres().remove(genre);
	}

	public boolean deleteGroup(String groupName) {
		Group group = getGroup(groupName);
		Category category = getCategory(group.getCategoryName());
		return group.getGenres().isEmpty() && category.getGroups().remove(group); //genious, yeah?
	}

	public boolean deleteCategory(String categoryName) {
		return categories.get(categoryName).getGroups().isEmpty() && categories.remove(categoryName) != null;
	}

	public static boolean addExecutor(String groupName) {
		Group group = getGroup(groupName);
		group.addExecutor();
		db().getCollection("category").updateOne(Document.parse("{'groups.name': '" + groupName + "'}"),
				Document.parse("{$inc : {'groups.$.executors' : 1}}"));
		return false;
	}
}
