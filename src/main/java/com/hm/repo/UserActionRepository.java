package com.hm.repo;

import com.hm.entity.UserAction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserActionRepository extends MongoRepository<UserAction, String> {
}
