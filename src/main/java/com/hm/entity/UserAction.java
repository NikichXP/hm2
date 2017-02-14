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
	private LocalDateTime performed;
	private String path;

	public UserAction () {
		this.id = UUID.randomUUID().toString();
		this.performed = LocalDateTime.now();
	}

	public UserAction (String userId, String path) {
		this();
		this.userId = userId;
		this.path = path;
	}

}
