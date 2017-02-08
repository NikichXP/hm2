package com.hm.repo;

import com.hm.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

	@Query("{'senderId' : ?0}")
	List<Message> getMessagesBySenderId(String id);

	@Query("{'recieptId' : ?0}")
	List<Message> getMessagesByRecieptId(String id);

	@Query("{'recieptId' : ?0, 'seen' : false}")
	List<Message> getUnreadMessagesById (String id);

}
