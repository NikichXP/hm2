package com.hm.repo;

import com.hm.entity.Moderator;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModeratorRepository extends MongoRepository <Moderator, String> {
}
