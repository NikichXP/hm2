package com.hm.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class News {

	@Id
	String id;
	String title;
	String text;
	String img;
	LocalDateTime posted;

	public News (String title, String text, String img) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.text = text;
		this.img = img;
		this.posted = LocalDateTime.now();
	}

}
