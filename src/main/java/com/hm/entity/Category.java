package com.hm.entity;

import com.hm.util.Generator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Category { //like "popular"

	@Id
	private String id;
	private String name;
	private ArrayList<Group> groups;

	public Category(String name) {
		this.id = Generator.genSmallId();
		this.name = name;
		this.groups = new ArrayList<>();
	}

}
