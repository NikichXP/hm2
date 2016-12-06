package com.hm.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {

	private Client client;
	private Product product;
	private Employee curator;

	private LocalDateTime time;
	private double price;
	private double paid;

}
