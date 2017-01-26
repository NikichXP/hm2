package com.hm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@NoArgsConstructor
public class User {

	@Id
	private String id;
	private String mail;
	private String pass; //this is encrypted
	private String userImg;
	private String entityClassName;

	private String name;

	public User (String mail, String pass) {
		if (mail.equals("admin@corp.com")) {
			this.id = "not-a-real-id";
		} else {
			this.id = UUID.randomUUID().toString();
		}
		this.pass = pass;
		this.mail = mail;
		this.name = "defaultName";
	}

	/**
	 * Adds new field in user JSON, which is suitable for {@link com.hm.api.FileAPI} methods
	 * @return userImg replaced . -> /
	 */
	public String getValidUserImg () {
		return userImg.replace(".", "/");
	}

}
