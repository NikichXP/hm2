package com.hm.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("/file")
public class FileAPI {

	@RequestMapping("/get/{userId}/{fileId}")
	private void getFile (HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") String userId,
	                      @PathVariable("fileId") String fileId) throws Exception {
		File file = new File("/"+userId + "/" + fileId);
		ServletContext sc = request.getSession().getServletContext();
		response.reset();
		response.setContentType(sc.getMimeType(file.getName()));
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		org.springframework.util.FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

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
