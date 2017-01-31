package com.hm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Client extends User {
	public Client(User user) {
		user.cloneTo(this);
		this.setEntityClassName("Client");
		user.setEntityClassName("Client");
	}
}
