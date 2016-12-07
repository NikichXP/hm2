package com.hm.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;

@Data
public class Group { //like "photographers"

	@Id
	private String id;
	private String name; //photographers
	private ArrayList<Genre> genres;

	private String categoryId;

	@Transient
	private Category category;

}
