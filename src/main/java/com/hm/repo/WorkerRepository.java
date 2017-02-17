package com.hm.repo;

import com.hm.entity.Worker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.stream.Stream;

public interface WorkerRepository extends MongoRepository<Worker, String> {

	@Query("{'isPro' : true}")
	Stream<Worker> getPro ();

	@Query("{'isPro' : true, 'city':?0}")
	Stream<Worker> getPro (String city);

	@Query("{'profession' : ?0}")
	Stream<Worker> getByProfession (String profession);
}
