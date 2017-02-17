package com.hm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Order {

	@Id
	private String id;
	private String productid;
	private String userId;
	private int hours;
	private int price;
	private int money;

	public Order (User user, Product product, int hours) {
		this.id = UUID.randomUUID().toString();
		if (product.isFixedPrice()) {
			this.price = product.getFinalPrice();
		} else {
			this.price = product.getFinalPrice() * hours;
		}
		this.productid = product.getId();
		this.userId = user.getId();
		this.money = 0;
		this.hours = hours;
	}

	public boolean getIsPaid () {
		return money >= price;
	}

	public boolean addMoney (int money) {
		this.money += money;
		return getIsPaid();
	}
}
