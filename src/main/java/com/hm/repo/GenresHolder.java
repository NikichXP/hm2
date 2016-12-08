package com.hm.repo;

import com.hm.entity.Category;
import com.hm.entity.Genre;
import com.hm.entity.Group;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Data
@Repository
public class GenresHolder {

	@Autowired
	public GenreRepository genreRepo;
	@Autowired
	public GroupRepository groupRepo;
	@Autowired
	public CategoryRepository categoryRepo;

	private HashMap<String, Genre> genres;
	private HashMap<String, Group> groups;
	private HashMap<String, Category> categories;

	public GenresHolder() {
		new Thread(() -> {
			boolean flag = true;
			while (flag) {
				try {
					Thread.sleep(100);
					if (genres == null) {
						genres = new HashMap<>();
						genreRepo.findAll().forEach(el -> {
							genres.put(el.getName(), el);
						});
					}
					if (groups == null) {
						groups = new HashMap<>();
						groupRepo.findAll().forEach(el -> {
							groups.put(el.getName(), el);
						});
					}
					if (categories == null) {
						categories = new HashMap<>();
						categoryRepo.findAll().forEach(el -> {
							categories.put(el.getName(), el);
						});
					}
					flag = false;
				} catch (Exception e) {
					genres = null;
					groups = null;
					categories = null;
				}
			}
		}).start();
	}

	public void addCategory (Category category) {
		categoryRepo.save(category);
		categories.put(category.getName(), category);
	}

	public void addGroup (Group group) {
		groupRepo.save(group);
		groups.put(group.getName(), group);
		if (!categories.containsValue(group.categoryEntity())) {
			group.categoryEntity().getGroups().add(group);
			addCategory(group.categoryEntity());
		} else {
			group.categoryEntity().getGroups().add(group);
			categoryRepo.save(group.categoryEntity());
		}
	}

	public void addGenre (Genre genre) {
		genreRepo.save(genre);
		genres.put(genre.getName(), genre);
		if (!groups.containsValue(genre.groupEntity())) {
			genre.groupEntity().getGenres().add(genre);
			addGroup(genre.groupEntity());
		} else {
			genre.groupEntity().getGenres().add(genre);
			groupRepo.save(genre.groupEntity());
		}
	}

	public Genre createGenre (String genreName, String groupName, String categoryName) {
		Category category;
		if (categories.get(categoryName) == null) {
			category = new Category(categoryName);
			categories.put(categoryName, category);
			addCategory(category);

		} else {
			category = categories.get(categoryName);
		}
		Group group;
		if (groups.get(groupName) == null) {
			group = new Group(groupName, category);
			groups.put(groupName, group);
			addGroup(group);
		} else {
			group = groups.get(groupName);
		}
		Genre genre;
		if (genres.get(genreName) == null) {
			genre = new Genre(genreName, group);
			genres.put(genreName, genre);
			addGenre(genre);
		} else {
			genre = genres.get(genreName);
		}
		return genre;
	}

	public Group getGroup(String groupId) {
		return groups.values().stream().filter(e -> e.getId().equals(groupId)).findFirst().orElse(null);
	}

	public Genre getGenre(String genreId) {
		return genres.values().stream().filter(e -> e.getId().equals(genreId)).findFirst().orElse(null);
	}

	public Category getCategory(String categoryId) {
		return categories.values().stream().filter(e -> e.getId().equals(categoryId)).findFirst().orElse(null);
	}

	//TODO Add more methods

}
