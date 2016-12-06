package com.hm.entity;

import lombok.*;

import java.util.ArrayList;

@Data
public class ServiceExecutor extends User {

	private String id;
	private String title;

	private double minPrice; //on link "starts from 200 UAH"
	private User author; //link to author's profile

	private ArrayList<Product> product;


}
