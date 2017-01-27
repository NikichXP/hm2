package com.hm.api;

import com.hm.entity.Photo;
import com.hm.repo.PhotosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/freephoto")
public class FreePhoto {

	@Autowired
	PhotosRepository repo;

	@GetMapping("/photographers") //FIXME THIS IS WTF?!?
	public ResponseEntity photographers() {
		return ResponseEntity.ok(repo.findAll()
				.stream()
				.map(Photo::getAuthorId)
				.distinct()
				.collect(Collectors.toList())
		);
	}

	@GetMapping("/getUsersPhotos")
	public ResponseEntity getUsersPhotos(@RequestParam("author")String author) {
		return ResponseEntity.ok(repo.getPhotosByAuthor(author));
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
