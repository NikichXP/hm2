package com.hm.api;

import com.hm.entity.LinkedPage;
import com.hm.repo.PagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pages")
@CrossOrigin
public class LinkedPagesAPI {

	@Autowired
	private PagesRepository pagesRepo;

	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable("id") String id) {
		return ResponseEntity.ok(pagesRepo.findOne(id));
	}

	@PostMapping("/create")
	public ResponseEntity createPage(@RequestParam("data") String[] data) {
		LinkedPage lp = new LinkedPage();
		for (String s : data) {
			lp.getMetadata().add(s);
		}
		pagesRepo.save(lp);
		return ResponseEntity.ok(lp);
	}

	@GetMapping("/create")
	public ResponseEntity createPageGet(@RequestParam("name") String name, @RequestParam("token") String token) {
		LinkedPage lp = new LinkedPage();
		lp.getMetadata().add("name = " + name);
		pagesRepo.save(lp);
		return ResponseEntity.ok(lp);
	}

	@RequestMapping("/addfield/{id}")
	public ResponseEntity addField (@PathVariable("id") String id, @RequestParam("token") String token, @RequestParam("data") String data) {
		LinkedPage lp = pagesRepo.findOne(id);
		lp.getMetadata().add(data);
		pagesRepo.save(lp);
		return ResponseEntity.ok(lp);
	}

}
