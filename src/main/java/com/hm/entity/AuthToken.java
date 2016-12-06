package com.hm.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthToken {

	private String sessionID;
	private User user;
	private long timeout;

	public AuthToken(User u) {
		this.sessionID = UUID.randomUUID().toString();
		this.user = u;
		this.timeout = System.currentTimeMillis() + 3_600_000; //1h
	}

}
