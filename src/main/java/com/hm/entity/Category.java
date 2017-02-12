package com.hm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Category { //like "popular"

	@Id
	private String id;
	private String name;
	private ArrayList<Group> groups;

	public Category(String name) {
		this.id = UUID.randomUUID().toString().substring(0, 13);
		this.name = name;
		this.groups = new ArrayList<>();
	}

}
