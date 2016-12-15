package com.hm.entity;

import com.hm.AppLoader;
import com.hm.repo.WorkerRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Product {

	@Id
	private String id;
	private ArrayList<String> photos;
	private String title;
	private String description;
	private String city;

	private String workerId;

	private String genreId;
	private String genreName;
	private String categoryId;
	private String categoryName;
	private String groupId;
	private String groupName;

	private boolean isActive;
	private double price;
	private boolean fixedPrice; //if false == can be ordered for 5 hours

	private boolean offeredPrice;
	private double discount;
	private double finalPrice;

	@Transient
	private static WorkerRepository workers = (WorkerRepository) AppLoader.ctx.getBean("workerRepository");

	public Product (String title, Genre genre, Worker worker) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.genreId = genre.getId();
		this.genreName = genre.getName();
		this.groupId = genre.groupEntity().getId();
		this.groupName = genre.groupEntity().getName();
		this.categoryId = genre.groupEntity().categoryEntity().getId();
		this.categoryName = genre.groupEntity().categoryEntity().getName();
		this.city = "Kiev";
		this.workerId = worker.getId();
	}

	public Product (String title, Genre genre, double price, Worker worker) {
		this(title, genre, worker);
		this.price = price;
		this.finalPrice = price;
	}

	public Product (String title, Genre genre, double price, Worker worker, String city) {
		this (title, genre, price, worker);
		this.city = city;
	}

	public void setDiscount(double discount) {
		if (discount == 0) {
			offeredPrice = false;
			finalPrice = price;
			this.discount = discount;
			return;
		} else if (discount > 1) {
			return;
		}
		this.offeredPrice = true;
		this.discount = discount;
		this.finalPrice = price * (1 - discount);
	}

	public Worker getWorkerEntity() {
		return workers.findOne(workerId);
	}
}
