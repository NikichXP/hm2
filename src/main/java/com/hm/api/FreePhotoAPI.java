package com.hm.api;

import com.hm.entity.Photo;
import com.hm.model.AuthController;
import com.hm.repo.PhotosRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.hm.manualdb.ConnectionHandler.db;

@RestController
@CrossOrigin
@RequestMapping("/api/freephoto")
public class FreePhotoAPI {

	@Autowired
	PhotosRepository repo;
	@Autowired
	AuthController authController;

	@GetMapping("/photographers")
	public ArrayList<String> photographers() {
		return (ArrayList<String>)db().getCollection("config").find(Document.parse("{'key':'freePhotoAuthors'}")).first().get("value");
	}

	@GetMapping("/getUsersPhotos")
	public ResponseEntity getUsersPhotos(@RequestParam("author")String author) {
		return ResponseEntity.ok(repo.getFreePhotosByAuthor(true, author));
	}

	@GetMapping("/getUserPhotosUrls")
	public ResponseEntity getUserPhotosUrls(@RequestParam("author")String author) {
		return ResponseEntity.ok(repo.getPhotosByAuthor(author)
				.parallelStream()
				.sorted((p1, p2) -> p1.getDate().compareTo(p2.getDate()))
				.map(Photo::getAuthorId)
				.map(url -> url.replace(".", "/"))
				.collect(Collectors.toList())
		);
	}

	@GetMapping("/addFreePhoto")
	public ResponseEntity addFreePhoto (@RequestParam("token") String token, @RequestParam("path") String path) {
		if (photographers().contains(authController.getUser(token).getId())) {
			Photo photo = new Photo(authController.getUser(token).getId(), path);
			photo.setFreePhoto(true);
			repo.save(photo);
			return ResponseEntity.ok(photo);
		}
		return ResponseEntity.status(403).body("Oops, something went wrong");
	}
	
}
