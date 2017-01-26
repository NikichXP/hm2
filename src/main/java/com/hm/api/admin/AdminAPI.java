package com.hm.api.admin;

import com.hm.repo.GenresHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/dev")
public class AdminAPI {

	@Autowired
	GenresHolder gh;

	@GetMapping("/create/genre")
	public ResponseEntity createGenre(@RequestParam("category") String category,
	                                  @RequestParam("group") String group,
	                                  @RequestParam("genre") String genre) {
		return ResponseEntity.ok(gh.createGenre(genre, group, category));
	}




}
