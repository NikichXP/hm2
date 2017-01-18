package com.hm.repo;

import com.hm.entity.Bid;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BidsRepository  extends MongoRepository<Bid, String> {



}
