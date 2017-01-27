package com.hm.entity;

import com.hm.AppLoader;
import com.hm.repo.GenresHolder;
import com.hm.repo.ProductRepository;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Worker extends User {

	private static ProductRepository prodRepo = (ProductRepository) AppLoader.ctx.getBean("productRepository");

	private String id;
	private boolean isPro;
	private String profession;
	private String city;

	private double minPrice; //on link "starts from 200 UAH"
	private ArrayList<String> productsIDs;


	public Worker(User user) {
		Arrays.stream(user.getClass().getMethods())
				.filter(method -> method.getName().startsWith("get"))
				.forEach(usermeth -> {
					String name = usermeth.getName().substring(3);
					Arrays.asList(this.getClass().getMethods()).stream()
							.filter(meth -> meth.getName().startsWith("set"))
							.filter(el -> el.getName().substring(3).equals(name))
							.findAny()
							.ifPresent(e -> {
								try {
									e.invoke(this, usermeth.invoke(user));
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							});
				});
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
