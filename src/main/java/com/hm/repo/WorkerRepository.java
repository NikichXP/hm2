package com.hm.repo;

import com.hm.entity.Worker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkerRepository extends MongoRepository<Worker, String> {
}
