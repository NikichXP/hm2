package com.hm.repo;

import com.hm.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ClientRepository extends MongoRepository<Client, String> {

}
