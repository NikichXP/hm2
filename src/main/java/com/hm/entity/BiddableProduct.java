package com.hm.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class BiddableProduct {

	@Id
	private String id;

	private String genre;
	private String title;
	private String description;
	private LocalDateTime deadline;
	private int workingHours;
	private int price;

	private Set<Node> bidders = new HashSet<>();

	public BiddableProduct() {
		this.id = UUID.randomUUID().toString();
	}

	@Data
	public static class Node {
		private String userId;
		private String userImg;
		private int bid;
		private String userName;
	}

}
