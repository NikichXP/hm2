package com.hm.repo;

import com.hm.entity.LinkedPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PagesRepository extends MongoRepository <LinkedPage, String> {
}
