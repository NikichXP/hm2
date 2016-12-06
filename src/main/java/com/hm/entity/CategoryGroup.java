package com.hm.entity;

import lombok.*;

import java.util.ArrayList;

@Data
public class CategoryGroup {

	private String name;
	private ArrayList<Group> groups;


}
