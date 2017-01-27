package com.hm.repo;

import com.hm.entity.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PhotosRepository extends MongoRepository <Photo, String> {

	@Query("{'authorId' : ?0}")
	List<Photo> getPhotosByAuthor (String authorId);
}
