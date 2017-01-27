package com.hm.api;

import com.hm.entity.Photo;
import com.hm.repo.PhotosRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.hm.manualdb.ConnectionHandler.db;

@RestController
@CrossOrigin
@RequestMapping("/freephoto")
public class FreePhotoAPI {

	@Autowired
	PhotosRepository repo;

	@GetMapping("/photographers")
	public ResponseEntity photographers() {
		return ResponseEntity.ok(db().getCollection("config").find(Document.parse("{'key':'freePhotoAuthors'}")).first().get("value"));
	}

	@GetMapping("/getUsersPhotos")
	public ResponseEntity getUsersPhotos(@RequestParam("author")String author) {
		return ResponseEntity.ok(repo.getFreePhotosByAuthor(true, author));
	}

	@GetMapping("/getUserPhotosUrls")
	public ResponseEntity getUserPhotosUrls(@RequestParam("author")String author) {
		return ResponseEntity.ok(repo.getPhotosByAuthor(author)
				.parallelStream()
				.map(Photo::getAuthorId)
				.map(url -> url.replace(".", "/"))
				.collect(Collectors.toList())
		);
	}
	
}
