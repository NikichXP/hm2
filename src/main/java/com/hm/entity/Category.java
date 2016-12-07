package com.hm.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Data
public class Category { //like "popular"

	@Id
	private String id;
	private String name;
	private ArrayList<Group> groups;


}
