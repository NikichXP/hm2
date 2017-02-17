package com.hm.api.admin;

import com.hm.entity.Product;
import com.hm.entity.Tender;
import com.hm.interceptor.Auth;
import com.hm.interceptor.LogAction;
import com.hm.repo.GenresHolder;
import com.hm.repo.OrderRepository;
import com.hm.repo.ProductRepository;
import com.hm.repo.TenderRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hm.manualdb.ConnectionHandler.db;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/crm")
@Auth("admin")
@LogAction("devadmin")
public class CRMAdminAPI {

	@Autowired
	private	GenresHolder gh;
	@Autowired
	private	ProductRepository prodRepo;
	@Autowired
	private	TenderRepository tenderRepo;
	@Autowired
	private	OrderRepository orderRepo;

	@GetMapping("/products")
	public ResponseEntity products() {
		return ResponseEntity.ok(prodRepo.findAll());
	}

	@GetMapping("/tenders")
	public ResponseEntity tenders() {
		return ResponseEntity.ok(tenderRepo.findAll());
	}

	@GetMapping("/addFreePhotoUser")
	public ResponseEntity addFreePhotoUser(@RequestParam("userid") String userid, @RequestParam("token") String token) {
		if (db().getCollection("config").find(Document.parse("{'key':'freePhotoAuthors'}")).first() == null) {
			db().getCollection("config").insertOne(Document.parse("{'key':'freePhotoAuthors', 'value':['"+userid+"']}"));
		} else {
			db().getCollection("config").updateOne(Document.parse("{'key':'freePhotoAuthors'}"),
					Document.parse("{$push : {'value' : '" + userid + "'}}"));
		}
		return ResponseEntity.ok(db().getCollection("config").find(Document.parse("{'key':'freePhotoAuthors'}")).first().toJson());
	}

	@GetMapping("/orders")
	public ResponseEntity orders() {
		return ResponseEntity.ok(orderRepo.findAll());
	}

	@GetMapping("/comment/tender")
	public ResponseEntity commentTender(@RequestParam("id") String id, @RequestParam("comment") String comment) {
		Tender t = tenderRepo.findOne(id);
		t.setComment(comment);
		tenderRepo.save(t);
		return ResponseEntity.ok(t);
	}

	@GetMapping("/validate/product")
	public ResponseEntity validateProduct(@RequestParam("prodid") String prodid, @RequestParam("status") boolean status) {
		Product p = prodRepo.findOne(prodid);
		p.setValidated(status);
		prodRepo.save(p);
		return ResponseEntity.ok(p);
	}

	@GetMapping("/validate/tender")
	public ResponseEntity validateTender(@RequestParam("tenderid") String tenderid, @RequestParam("status") boolean status) {
		Tender t = tenderRepo.findOne(tenderid);
		t.setValidated(status);
		tenderRepo.save(t);
		return ResponseEntity.ok(t);
	}

	@GetMapping("/activate/tender")
	public ResponseEntity activateTender(@RequestParam("tenderid") String tenderid, @RequestParam("status") boolean status) {
		Tender t = tenderRepo.findOne(tenderid);
		t.setActive(status);
		tenderRepo.save(t);
		return ResponseEntity.ok(t);
	}

	@GetMapping("/validate/offer")
	public ResponseEntity validateOffer(@RequestParam("prodid") String prodid, @RequestParam("status") boolean status) {
		Product p = prodRepo.findOne(prodid);
		p.setOfferValid(status);
		prodRepo.save(p);
		return ResponseEntity.ok(p);
	}

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
