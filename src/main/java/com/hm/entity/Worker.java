package com.hm.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;

@Data
@NoArgsConstructor
public class Worker extends User {


	private String id;
	private String title;

	private double minPrice; //on link "starts from 200 UAH"
	private User author; //link to author's profile

	private ArrayList<Product> product;


	public Worker(User user) {
		Arrays.asList(user.getClass().getMethods()).stream()
				.filter(method -> method.getName().startsWith("get"))
				.forEach(usermeth -> {
					String name = usermeth.getName().substring(3);
					Arrays.asList(this.getClass().getMethods()).stream()
							.filter(meth -> meth.getName().startsWith("set"))
							.filter(el -> el.getName().substring(3).equals(name))
							.findAny().
							ifPresent(e -> {
								try {
									e.invoke(this, usermeth.invoke(user));
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							});
				});
		this.setEntityClassName("Worker");
		user.setEntityClassName("Worker");
	}
}
