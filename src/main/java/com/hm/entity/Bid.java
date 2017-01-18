package com.hm.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Bid {

	@Id
	private String id;

	private String userId;
	private String productId;
	private LocalDateTime time;
	private double price;

	public Bid (User user, Product product, double price) {
		this.userId = user.getId();
		this.productId = product.getId();
		this.price = price;
		this.time = LocalDateTime.now();
	}

}
