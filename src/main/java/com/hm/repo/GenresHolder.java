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
	public CategoryRepository categoryRepo;

	private HashMap<String, Category> categories;

	public GenresHolder() {
		new Thread(() -> {
			boolean flag = true;
			while (flag) {
				try {
					Thread.sleep(100);
					if (categories == null) {
						categories = new HashMap<>();
						categoryRepo.findAll().forEach(el -> {
							categories.put(el.getName(), el);
						});
					}
					flag = false;
				} catch (Exception e) {
					categories = null;
				}
			}
		}).start();
	}

	public void save() {
		categories.values().forEach(e -> {
			categoryRepo.save(e);
		});
	}

	public void addCategory (Category category) {
		categoryRepo.save(category);
		categories.put(category.getName(), category);
	}

	public void addGroup (Group group) {
		if (categories.get(group.getCategoryName()) == null) {
			group.categoryEntity().getGroups().add(group);
			addCategory(group.categoryEntity());
		} else {
			group.categoryEntity().getGroups().add(group);
			categoryRepo.save(group.categoryEntity());
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
