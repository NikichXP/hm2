package com.hm.repo;

import com.hm.entity.Category;
import com.hm.entity.Genre;
import com.hm.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by NikichXP on 08.12.2016.
 */
public class GenresHolder {

	@Autowired
	private GenreRepository genreRepo;
	@Autowired
	private GroupRepository groupRepo;
	@Autowired
	private CategoryRepository categoryRepo;

	private List<Genre> genres = genreRepo.findAll();
	private List<Group> groups = groupRepo.findAll();
	private List<Category> categories = categoryRepo.findAll();

	public void addCategory (Category category) {
		categoryRepo.save(category);
	}

	//TODO Add more methods


	public static interface GenreRepository extends MongoRepository<Genre, String> {}
	public static interface GroupRepository extends MongoRepository<Group, String> {}
	public static interface CategoryRepository extends MongoRepository<Category, String> {}
}
