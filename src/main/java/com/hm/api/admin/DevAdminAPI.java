package com.hm.api.admin;

import com.hm.repo.GenresHolder;
import com.hm.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/dev")
public class DevAdminAPI {

	@Autowired
	GenresHolder gh;
	@Autowired
	ProductRepository prodRepo;

	@GetMapping("/genre/create")
	public ResponseEntity createGenre(@RequestParam("category") String category,
	                                  @RequestParam("group") String group,
	                                  @RequestParam("genre") String genre) {
		return ResponseEntity.ok(gh.createGenre(genre, group, category));
	}

	@GetMapping("/genre/delete")
	public ResponseEntity deleteGenre(@RequestParam("genre") String genreName) {
		if (prodRepo.listCustom1ArgQuery("genreName", genreName).count() != 0) {
			return ResponseEntity.ok("Существуют продукты с таким жанром.");
		}
		return ResponseEntity.ok("Success: " + gh.deleteGenre(genreName));
	}

	@GetMapping("/group/delete") //TODO #discuss: cascade or only if empty
	public ResponseEntity deleteGroup(@RequestParam("group") String groupName) {
		if (prodRepo.listCustom1ArgQuery("groupName", groupName).count() != 0) {
			return ResponseEntity.ok("Существуют продукты в данной группе.");
		}
		return ResponseEntity.ok("Success: " + gh.deleteGroup(groupName));
	}

	@GetMapping("/category/delete") //TODO #discuss: cascade or only if empty
	public ResponseEntity deleteCategory(@RequestParam("category") String categoryName) {
		if (prodRepo.listCustom1ArgQuery("categoryName", categoryName).count() != 0) {
			return ResponseEntity.ok("Существуют продукты в данной категории.");
		}
		return ResponseEntity.ok("Success: " + gh.deleteCategory(categoryName));
	}

	@GetMapping("/invite") //TODO Need to implement this
	public ResponseEntity inviteUserViaMail(@RequestParam("mail") String mail, @RequestParam("token") String token) {
		return ResponseEntity.ok(null);
	}


}
