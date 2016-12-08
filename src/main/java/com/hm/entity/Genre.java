package com.hm.entity;

import com.hm.AppLoader;
import com.hm.repo.GenresHolder;
import com.hm.util.Generator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@Data
@NoArgsConstructor
public class Genre {

	@Id
	private String id;
	private String name;

	private String categoryId;
	private String categoryName;
	private String groupId;
	private String groupName;

	@Transient
	private static GenresHolder holder = (GenresHolder) AppLoader.ctx.getBean("genresHolder");

	public Genre (String name, Group group) {
		this.id = Generator.genId();
		this.name = name;
		this.groupId = group.getId();
		this.groupName = group.getName();
		this.categoryId = group.getCategoryId();
		this.categoryName = group.getCategoryName();
	}

	public Group groupEntity() {
		return holder.getGroup(groupId);
	}

}
