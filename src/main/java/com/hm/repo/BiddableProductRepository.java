package com.hm.repo;

import com.hm.entity.BiddableProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BiddableProductRepository extends MongoRepository<BiddableProduct, String> {
}
