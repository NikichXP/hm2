package com.hm.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.*;

@Data
@NoArgsConstructor
public class Genre {

	@Id
	private String id;
	private String name;
	private HashMap<ServiceExecutor, Product> users;

	private String categoryId;
	private String categoryName;
	private String groupId;
	private String groupName;

	@Transient
	private Category category;
	@Transient
	private Group group;



}
