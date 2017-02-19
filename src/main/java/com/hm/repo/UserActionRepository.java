package com.hm.repo;

import com.hm.entity.UserAction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserActionRepository extends MongoRepository<UserAction, String> {

	@Query("{?0 : ?1}")
	List<UserAction> custom1ArgQuery (String arg0, String val0);

	@Query("{?0 : ?1, ?2 : ?3}")
	List<UserAction> custom2ArgQuery (String arg0, String val0, String arg1, String val1);

	@Query("{'userIP': ?0}")
	List<UserAction> findByIP(String ip);

	@Query("{'userId' : ?0}")
	List<UserAction> findByUserID (String id);

}
