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
		user.cloneTo(this);
		this.accessLevel = 0;
		this.setEntityClassName("Moderator");
		user.setEntityClassName("Moderator");
	}
}
