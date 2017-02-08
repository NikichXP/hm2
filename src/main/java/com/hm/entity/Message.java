package com.hm.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Message {
	@Id
	private String id;
	private String senderId;
	private String senderName;
	private String recieptId;
	private String recieptName;

	private boolean seen;
	private String theme;
	private String text;
	private LocalDateTime sent;

	public Message() {
		this.id = UUID.randomUUID().toString();
		this.seen = false;
		this.sent = LocalDateTime.now();
	}

	public String getSentString () {
		return sent.toString();
	}

	public void setDelegates (User sender, User reciept) {
		this.senderId = sender.getId();
		this.senderName = sender.getName();
		this.recieptId = reciept.getId();
		this.recieptName = reciept.getName();
	}

}