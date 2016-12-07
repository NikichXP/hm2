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
		this.sessionID = UUID.randomUUID().toString();
		this.user = user;
		this.timeout = System.currentTimeMillis() + 3_600; //1h/1000
	}

}
