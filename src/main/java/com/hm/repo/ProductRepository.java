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

	@Query("{?0 : ?1}")
	public List<Product> listCustomQuery(String arg1, Object arg2);

	@Query("{?0 : ?1, ?2 : ?3}")
	public List<Product> listCustomTwoArgQuery(String key0, Object value0,
	                                           String key1, Object value1);

	@Query("{?0 : ?1, ?2 : ?3, ?4 : ?5}")
	public List<Product> listCustomThreeArgQuery(String key0, Object value0,
	                                             String key1, Object value1,
	                                             String key2, Object value2);

	@Query("{?0 : ?1, ?2 : ?3, ?4 : ?5, ?6 : ?7}")
	public List<Product> listCustomFourArgQuery(String key0, Object value0,
	                                            String key1, Object value1,
	                                            String key2, Object value2,
	                                            String key3, Object value3);

}
