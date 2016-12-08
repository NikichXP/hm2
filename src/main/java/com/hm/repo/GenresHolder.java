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
		if (!categories.containsValue(group.getCategory())) {
			group.getCategory().getGroups().add(group);
			addCategory(group.getCategory());
		} else {
			group.getCategory().getGroups().add(group);
			categoryRepo.save(group.getCategory());
		}
	}

	public void addGenre (Genre genre) {
		genreRepo.save(genre);
		genres.put(genre.getName(), genre);
		if (!groups.containsValue(genre.getGroup())) {
			genre.getGroup().getGenres().add(genre);
			addGroup(genre.getGroup());
		} else {
			genre.getGroup().getGenres().add(genre);
			groupRepo.save(genre.getGroup());
		}
	}

	public Genre createGenre (String genreName, String groupName, String categoryName) {
		Category category;
		if (categories.get(categoryName) == null) {
			category = new Category(categoryName);
			addCategory(category);
		} else {
			category = categories.get(categoryName);
		}
		Group group;
		if (groups.get(groupName) == null) {
			group = new Group(groupName, category);
			addGroup(group);
		} else {
			group = groups.get(groupName);
		}
		Genre genre;
		if (genres.get(genreName) == null) {
			genre = new Genre(genreName, group);
			addGenre(genre);
		} else {
			genre = genres.get(genreName);
		}
		return genre;
	}

	//TODO Add more methods

}
