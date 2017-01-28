package com.hm.repo;

import com.hm.entity.News;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewsRepository extends MongoRepository <News, String> {
}
