package com.hm.api;

import com.hm.AppLoader;
import com.hm.entity.Product;
import com.hm.repo.GenresHolder;
import com.hm.repo.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/config")
public class ConfigAPI {

	@RequestMapping("/list/city")
	public ResponseEntity listCities () {
		return ResponseEntity.ok(new String []{"Киев", "Львов", "Одесса"});
	}

	@GetMapping("/list/genre")
	public ResponseEntity listGenres () {
		return ResponseEntity.ok(GenresHolder.getCategories());
	}

	@GetMapping("/list/offer/group")
	public ResponseEntity listOfferGroups () {
		return ResponseEntity.ok(AppLoader.ctx.getBean(ProductRepository.class)
				.listCustomQuery("offeredPrice", true)
				.stream()
				.map(Product::getGroupName)
				.distinct()
				.collect(Collectors.toList())
		);
	}

	@GetMapping("/list/offer/genre/{group}")
	public ResponseEntity listOfferGenres (@PathVariable("group") String group) {
		return ResponseEntity.ok(AppLoader.ctx.getBean(ProductRepository.class)
				.listCustomTwoArgQuery("offeredPrice", true, "groupName", group)
				.stream()
				.map(Product::getGroupName)
				.distinct()
				.collect(Collectors.toList())
		);
	}

}
