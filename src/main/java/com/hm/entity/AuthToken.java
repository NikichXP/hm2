package com.hm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AuthToken {

	@Id
	private String sessionID;
	private User user;
	private long timeout;

	public AuthToken(User user) {
		if (user.getId().startsWith("test") || user.getId().startsWith("not-a-real")) { //TODO TEST PURPOSE
			this.sessionID = user.getId(); //FIXME Delete this
		} else {
			this.sessionID = UUID.randomUUID().toString();
		}
		this.user = user;
		switch (user.getEntityClassName().toLowerCase()) {
			case "moderator":
				this.timeout = System.currentTimeMillis() + (3600 * 1000L * 24);
				break;
			default:
				this.timeout = System.currentTimeMillis() + (3600 * 1000L);
				break;
		}

	}

}
