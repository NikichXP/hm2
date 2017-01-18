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

	public User (String mail, String pass) {
		if (mail.equals("admin@corp.com")) {
			this.id = "not-a-real-id";
		} else {
			this.id = UUID.randomUUID().toString();
		}
		this.mail = mail;
		this.pass = pass;
	}

}
