package com.hm.entity;

import com.hm.AppLoader;
import com.hm.repo.GenresHolder;
import com.hm.repo.ProductRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.ApplicationContext;
import org.springframework.data.annotation.Transient;

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

	private double minPrice; //on link "starts from 200 UAH"
	private ArrayList<ProductInfo> products;


	public Worker(User user) {
		user.cloneTo(this);
		this.setEntityClassName("Worker");
		user.setEntityClassName("Worker");
		this.isPro = false;
		products = new ArrayList<>();
	}

//	public void cloneOf (User user) {
//		this.id = user.getId();
//		this.mail = user.getMail();
//		this.userImg = user.getUserImg();
//		this.name = user.getName();
//		this.regDate = user.getRegDate();
//		this.description = user.getDescription();
//		this.city = user.getCity();
//	}

	public void addProduct(Product product) {
		if (this.profession == null) {
			this.profession = product.getGroupName();
			GenresHolder.addExecutor(product.getGroupName());
		}
		if (this.city == null) {
			this.city = product.getCity();
		}
		this.products.add(new ProductInfo(product));
		this.minPrice = ((minPrice == 0) ? product.getPrice() : Math.min(minPrice, product.getPrice()));
	}

	public List<Product> listProducts () {
		return prodRepo.listByWorkerId(this.id);
	}

	@Data
	@NoArgsConstructor
	public static class ProductInfo {

		private static final ApplicationContext app = AppLoader.ctx;
		private String id;
		@Transient
		private Product nested;

		public ProductInfo (Product prod) {
			this.setId(prod.getId());
		}

		public String getName() {
			if (nested == null) {
				nested = app.getBean(ProductRepository.class).findOne(this.id);
			}
			return nested.getTitle();
		}

		public String getGenreName() {
			if (nested == null) {
				nested = app.getBean(ProductRepository.class).findOne(this.id);
			}
			return nested.getGenreName();
		}

		public int getPrice () {
			if (nested == null) {
				nested = app.getBean(ProductRepository.class).findOne(this.id);
			}
			return nested.getPrice();
		}

		public String getDescription() {
			if (nested == null) {
				nested = app.getBean(ProductRepository.class).findOne(this.id);
			}
			return nested.getDescription();
		}
	}
}
