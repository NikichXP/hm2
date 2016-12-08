package com.hm.entity;

import com.hm.util.Generator;
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

	public Genre (String name, Group parent) {
		this.id = Generator.genId();
		this.name = name;
		this.group = parent;
		this.groupId = parent.getId();
		this.groupName = parent.getName();
		this.category = group.getCategory();
		this.categoryId = group.getCategoryId();
		this.categoryName = group.getCategoryName();
	}

}
