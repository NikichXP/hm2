package com.hm.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BiddableProductRepository extends MongoRepository<BiddableProductRepository, String> {
}
