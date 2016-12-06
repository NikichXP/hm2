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

	public User (String mail, String pass) {
		this.id = UUID.randomUUID().toString();
		this.mail = mail;
		this.pass = pass;
	}

}
