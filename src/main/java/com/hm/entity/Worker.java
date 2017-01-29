package com.hm.entity;

import com.hm.AppLoader;
import com.hm.repo.GenresHolder;
import com.hm.repo.ProductRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Worker extends User {

	private static ProductRepository prodRepo = (ProductRepository) AppLoader.ctx.getBean("productRepository");
	private boolean isPro;
	private String profession;
	private String city;

	private double minPrice; //on link "starts from 200 UAH"
	private ArrayList<String> productsIDs;


	public Worker(User user) {
		this.id = user.getId();
		this.mail = user.getMail();
		this.userImg = user.getUserImg();
		this.name = user.getName();
		this.setEntityClassName("Worker");
		user.setEntityClassName("Worker");
		this.isPro = false;
		productsIDs = new ArrayList<>();
	}

	public void addProduct(Product product) {
		if (this.profession == null) {
			this.profession = product.getGroupName();
			GenresHolder.addExecutor(product.getGroupName());
		}
		if (this.city == null) {
			this.city = product.getCity();
		}
		this.productsIDs.add(product.getId());
		this.minPrice = ((minPrice == 0) ? product.getPrice() : Math.min(minPrice, product.getPrice()));
	}

	public List<Product> listProducts () {
		return prodRepo.listByWorkerId(this.id);
	}
}
