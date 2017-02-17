package com.hm.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserAction {

	@Id
	private String id;
	private String userId;
	private String userIP;
	private LocalDateTime performed;
	private String path;
	private String action;

	public UserAction () {
		this.id = UUID.randomUUID().toString();
		this.performed = LocalDateTime.now();
	}

	public UserAction (String userId, String path) {
		this();
		this.userId = userId;
		this.path = path;
	}

	public UserAction (String userIP, String userId, String path, String action) {
		this(userId, path);
		this.action = action;
		this.userIP = userIP;
	}

}
