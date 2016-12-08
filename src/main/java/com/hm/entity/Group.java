package com.hm.entity;

import com.hm.util.Generator;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Group { //like "photographers"

	@Id
	private String id;
	private String name; //photographers
	private ArrayList<Genre> genres;

	private String categoryId;
	private String categoryName;

	@Transient
	private Category category;

	public Group(String name, Category category) {
		this.id = Generator.genSmallId();
		this.name = name;
		this.category = category;
		this.categoryId = category.getId();
		this.categoryName = category.getName();
		this.genres = new ArrayList<>();
	}
}
