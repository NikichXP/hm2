package com.hm.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class BiddableProduct {

	@Id
	private String id;

	private String genre;
	private String title;
	private String description;
	private LocalDateTime deadline;
	private double workingHours;
	private double price;

	private Set<Node> bidders = new HashSet<>();

	@Data
	public static class Node {
		private String userId;
		private String userImg;
	}

}
