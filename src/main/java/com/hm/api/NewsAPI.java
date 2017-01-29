package com.hm.api;

import com.hm.entity.News;
import com.hm.model.AuthController;
import com.hm.repo.NewsRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/news")
public class NewsAPI {

	@Autowired
	private NewsRepository newsRepo;
	@Autowired
	private AuthController authController;

	@PostMapping("/postNews")
	public ResponseEntity postNews(@RequestParam("title") String title, @RequestParam("text") String text,
	                               @RequestParam("token") String token,
	                               @RequestParam(value = "img", defaultValue = "", required = false) String img) {
		//TODO Add token here
		News n = new News(title, text, img);
		newsRepo.save(n);
		return ResponseEntity.ok("Done");
	}

	@GetMapping("/getNews")
	public ResponseEntity getNews(@RequestParam(value = "id", required = false) String id) {
		if (id == null) {
			val ret = newsRepo.findAll();
			ret.sort((n1, n2) -> n1.getPosted().compareTo(n2.getPosted()));
			return ResponseEntity.ok(ret);
		}
		return ResponseEntity.ok(newsRepo.findOne(id));
	}

}
