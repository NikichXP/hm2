package com.hm.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Photo {

	@Id
	String id;
	String authorId;
	String url;
	LocalDate date;
	boolean isFreePhoto;

	public Photo(String authorId, String url) {
		this.id = UUID.randomUUID().toString();
		this.authorId = authorId;
		this.url = url;
		this.date = LocalDate.now();
	}

}
