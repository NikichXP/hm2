package com.hm.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Photo {

	@Id
	String id;
	String authorId;
	String url;
	LocalDate date;
	boolean isFreePhoto;

}
