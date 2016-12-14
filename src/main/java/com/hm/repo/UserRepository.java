package com.hm.repo;

import com.hm.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

	@Query("{'mail' : ?0, 'pass' : ?1}")
	public User findByMailAndPass (String mail, String pass);
	public User findByMail (String mail);

}
