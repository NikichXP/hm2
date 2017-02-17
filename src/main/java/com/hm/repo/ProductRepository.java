package com.hm.repo;

import com.hm.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface ProductRepository extends MongoRepository <Product, String> {

	@Query("{ 'city' : ?0, 'groupName' : ?1, 'isValidated' : true }")
	public List<Product> listProductsInCity (String city, String group);

	@Query("{ 'workerId' : ?0 }")
	public List<Product> listByWorkerId (String workerId);

	@Query("{?0 : ?1}")
	public Stream<Product> listCustom1ArgQuery(String arg1, Object arg2);

	@Query("{?0 : ?1, ?2 : ?3}")
	public Stream<Product> listCustom2ArgQuery(String key0, Object value0,
	                                         String key1, Object value1);

	@Query("{?0 : ?1, ?2 : ?3, ?4 : ?5}")
	public Stream<Product> listCustom3ArgQuery(String key0, Object value0,
	                                         String key1, Object value1,
	                                         String key2, Object value2);

	@Query("{?0 : ?1, ?2 : ?3, ?4 : ?5, ?6 : ?7}")
	public Stream<Product> listCustom4ArgQuery(String key0, Object value0,
	                                           String key1, Object value1,
	                                           String key2, Object value2,
	                                           String key3, Object value3);

	@Query("{?0 : ?1, ?2 : ?3, ?4 : ?5, ?6 : ?7, ?8 : ?9}")
	public Stream<Product> listCustom5ArgQuery(String key0, Object value0,
	                                           String key1, Object value1,
	                                           String key2, Object value2,
	                                           String key4, Object value4,
	                                           String key3, Object value3);

}
