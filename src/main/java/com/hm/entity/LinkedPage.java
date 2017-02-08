package com.hm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Data
public class LinkedPage {

	@Id
	private String id;
	private ArrayList<String> metadata;

	public LinkedPage() {
		this.metadata = new ArrayList<>();
	}
}
