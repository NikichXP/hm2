package com.hm.repo;

import com.hm.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository <Product, String> {

	@Query("{ 'city' : ?0, 'groupName' : ?1 }")
	public List<Product> listProductsInCity (String city, String group);

	@Query("{ 'workerId' : ?0 }")
	public List<Product> listByWorkerId (String workerId);

	@Query("{'offeredPrice' : ?0 }")
	public List<Product> listWithOffer(boolean offer);

}
