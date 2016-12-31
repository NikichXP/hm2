package com.hm.repo;

import com.google.gson.Gson;
import com.hm.entity.Category;
import com.hm.entity.Genre;
import com.hm.entity.Group;
import lombok.Data;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

import static com.hm.manualdb.ConnectionHandler.db;

@Data
@Repository
public class GenresHolder {

	@Autowired
	Gson gson;
	private HashMap<String, Category> categories;

	public GenresHolder() {
		categories = new HashMap<>();
//		db().getCollection("category").find().forEach((Block<? super Document>) doc -> {
//			Category category = gson.fromJson(doc.toJson(), Category.class);
//			categories.put(category.getName(), category);
//		});
	}

	public void save() {
		categories.values().forEach(e -> {
			db().getCollection("category").insertOne(Document.parse(gson.toJson(e)));
		});
	}

	public void addCategory (Category category) {
		db().getCollection("category").insertOne(Document.parse(gson.toJson(category)));
		categories.put(category.getName(), category);
	}

	public void addGroup (Group group) {
		if (categories.get(group.getCategoryName()) == null) {
			group.categoryEntity().getGroups().add(group);
			addCategory(group.categoryEntity());
		} else {
			group.categoryEntity().getGroups().add(group);
			db().getCollection("category").insertOne(Document.parse(gson.toJson(group.categoryEntity())));
		}
	}

	public void addGenre (Genre genre) {
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
				.findFirst().ifPresent(tar -> tar.getGenres().add(genre));
	}

	private Group createGroup(String groupName, Category category) {
		return category.getGroups().stream().filter(group -> group.getName().equals(groupName)).findFirst().orElseGet(() -> {
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

	public Genre createGenre (String genreName, String groupName, String categoryName) {
		Category category = createCategory(categoryName);
		Group group = createGroup(groupName, category);
		return group.getGenres().stream().filter(g -> g.getName().equals(genreName)).findFirst().orElseGet(() -> {
			Genre genre = new Genre(genreName, group);
			group.getGenres().add(genre);
			save();
			return genre;
		});
	}

	public Group getGroup(String groupName) {
		return categories.values().stream().flatMap(cat -> cat.getGroups()
				.stream()).filter(group -> group.getName().equals(groupName) || group.getId().equals(groupName)).findFirst().orElse(null);
	}

	public Genre getGenre(String genreName) {
		return categories.values().parallelStream()
				.flatMap(category -> category.getGroups().parallelStream())
				.flatMap(group -> group.getGenres().parallelStream())
				.filter(genre -> genre.getName().equals(genreName)).findFirst().orElse(null);
	}

	public Category getCategory(String categoryId) {
		return categories.values().stream().filter(e -> e.getId().equals(categoryId)).findFirst().orElse(null);
	}
}
