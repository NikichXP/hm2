package com.hm.repo;

import com.hm.entity.Tender;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TenderRepository extends MongoRepository<Tender, String> {



}
