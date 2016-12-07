package com.hm.api;

import com.hm.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("product")
public class ProductAPI {

	@Autowired
	private ProductRepository prodRepo;

	@RequestMapping("list/{group}/{city}")
	public ResponseEntity listInCity (@PathVariable("city") @NotNull String cityName, @PathVariable("group") String group) {
		return ResponseEntity.ok(prodRepo.listProductsInCity(cityName, group));
	}

}
