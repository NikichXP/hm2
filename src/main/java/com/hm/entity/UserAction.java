package com.hm.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
	private String method;
	private Map<String, String> params;

	public UserAction() {
		this.id = UUID.randomUUID().toString();
		this.performed = LocalDateTime.now();
	}

	public UserAction(String userId, String path) {
		this();
		this.userId = userId;
		this.path = path;
	}

	public UserAction(String userIP, String userId, String path, String action, Map<String, String[]> parameterMap) {
		this(userId, path);
		this.action = action;
		this.userIP = userIP;
		this.params = new HashMap<>();
		parameterMap.keySet().forEach(key -> {
			String[] vals = parameterMap.get(key);
			if (vals.length == 1) {
				params.put(key, vals[0]);
			} else {
				params.put(key, Arrays.toString(vals));
			}
		});
	}

	public UserAction(String userIP, String userId, String path, String action, String method, Map<String, String[]> parameterMap) {
		this(userIP, userId, action, path, parameterMap);
		this.method = method;
	}

}
