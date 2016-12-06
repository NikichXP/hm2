package com.hm.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Product {

	private ArrayList<String> photos;
	private String description;
	private double price;
	private boolean hasPrice;
	private boolean fixedPrice; //if false == can be ordered for 5 hours

}
