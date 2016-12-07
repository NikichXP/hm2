package com.hm.repo;

import com.hm.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByMailAndPass (String mail, String pass);
	public User findByMail (String mail);

}
