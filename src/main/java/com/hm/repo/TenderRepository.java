package com.hm.repo;

import com.hm.entity.Tender;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface TenderRepository extends MongoRepository<Tender, String> {

	List<Tender> findByDeadlineAfter(LocalDate date);

}
