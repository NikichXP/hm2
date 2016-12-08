package com.hm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Product {

	@Id
	private String id;
	private ArrayList<String> photos;
	private String title;
	private String description;
	private String city;

	private String genreId;
	private String genreName;
	private String categoryId;
	private String categoryName;
	private String groupId;
	private String groupName;

	@Transient
	private Genre genre;
	@Transient
	private Category category;
	@Transient
	private Group group;



	private double price;
	private boolean hasPrice;
	private boolean fixedPrice; //if false == can be ordered for 5 hours

	public Product (String title, Genre genre) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.genre = genre;
		this.genreId = genre.getId();
		this.genreName = genre.getName();
		this.group = genre.groupEntity();
		this.groupId = group.getId();
		this.groupName = genre.groupEntity().getName();
		this.category = group.categoryEntity();
		this.categoryId = category.getId();
		this.categoryName = this.genre.groupEntity().categoryEntity().getName();
		this.city = "Kiev";
	}

	public Product (String title, Genre genre, double price) {
		this(title, genre);
		this.price = price;
	}

	public Product (String title, Genre genre, double price, String city) {
		this (title, genre, price);
		this.city = city;
	}

//	public Genre getGenre () {
//
//	}
}
