package com.hm.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class User {

	@Id
	String id;
	String mail;
	private String pass; //this is encrypted
	String userImg;
	String entityClassName;
	String name;
	String regdate;

	public User (String mail, String pass) {
		if (mail.equals("admin@corp.com")) {
			this.id = "not-a-real-id";
		} else {
			this.id = UUID.randomUUID().toString();
		}
		this.pass = pass;
		this.mail = mail;
		this.name = "defaultName";
		this.regdate = LocalDate.now().toString();
	}

	/**
	 * Adds new field in user JSON, which is suitable for {@link com.hm.api.FileAPI} methods
	 * @return userImg replaced . -> /
	 */
	public String getValidUserImg () {
		return userImg.replace(".", "/");
	}

}
