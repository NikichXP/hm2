package com.hm.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileAPI {

//	private final StorageService storageService;
//
//	@Autowired
//	public FileAPI(StorageService storageService) {
//		this.storageService = storageService;
//	}
//
//	@GetMapping("/")
//	public String listUploadedFiles(Model model) throws IOException {
//
//		model.addAttribute("files", storageService
//				.loadAll()
//				.map(path ->
//						MvcUriComponentsBuilder
//								.fromMethodName(FileAPI.class, "serveFile", path.getFileName().toString())
//								.build().toString())
//				.collect(Collectors.toList()));
//
//		return "uploadForm";
//	}
//
//	@GetMapping("/files/{filename:.+}")
//	@ResponseBody
//	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//
//		Resource file = storageService.loadAsResource(filename);
//		return ResponseEntity
//				.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
//				.body(file);
//	}
//
//	@PostMapping("/")
//	public String handleFileUpload(@RequestParam("file") MultipartFile file,
//	                               RedirectAttributes redirectAttributes) {
//
//		storageService.store(file);
//		redirectAttributes.addFlashAttribute("message",
//				"You successfully uploaded " + file.getOriginalFilename() + "!");
//
//		return "redirect:/";
//	}
//
//	@ExceptionHandler(StorageFileNotFoundException.class)
//	public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
//		return ResponseEntity.notFound().build();
//	}

}
