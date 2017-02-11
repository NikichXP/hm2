package com.hm.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Tender {

	@Id
	private String id;

	private User creator;
	private String genre;
	private String group;
	private String title;
	private String description;
	private String city;
	private LocalDate deadline;
	private int workingHours;
	private int price;

	private Set<Node> bidders;// = new HashSet<>();

	public Tender() {
		bidders = new HashSet<>();
		this.id = UUID.randomUUID().toString();
	}

	public void bid (Worker worker, int bid) {
		Node node = new Node();
		node.setUserId(worker.getId());
		node.setUserImg(worker.getUserImg());
		node.setUserName(worker.getName());
		node.setBid(bid);
		bidders.add(node);
	}

	@Data
	public static class Node {
		private String userId;
		private String userImg;
		private int bid;
		private String userName;
	}

	public String getDeadlineString () {
		return deadline.toString();
	}

}
