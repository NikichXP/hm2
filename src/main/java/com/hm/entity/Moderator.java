package com.hm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Moderator extends User {

	private int accessLevel;

	public Moderator(User user) {
		this.id = user.getId();
		this.mail = user.getMail();
		this.userImg = user.getUserImg();
		this.name = user.getName();
		this.accessLevel = 0;
		this.setEntityClassName("Moderator");
		user.setEntityClassName("Moderator");
	}
}
