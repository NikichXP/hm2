package com.hm.entity;

import com.hm.AppLoader;
import com.hm.repo.GenresHolder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Group { //like "photographers"

	@Id
	private String id;
	private String name; //photographers
	private ArrayList<Genre> genres;

	private String categoryId;
	private String categoryName;

	private int executors;

	@Transient
	private static GenresHolder holder = (GenresHolder) AppLoader.ctx.getBean("genresHolder");

	public Group(String name, Category category) {
		this.id = UUID.randomUUID().toString().substring(0, 13);
		this.name = name;
		this.categoryId = category.getId();
		this.categoryName = category.getName();
		this.genres = new ArrayList<>();
		this.executors = 0;
	}

	public Category categoryEntity() {
		return holder.getCategory(categoryId);
	}

	public void addExecutor() {
		this.executors++;
	}
}
