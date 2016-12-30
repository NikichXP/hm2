package com.hm.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
public class FileAPI {

	@RequestMapping("/get/{userId}/{fileId}")
	public void getFile(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") String userId,
	                     @PathVariable("fileId") String fileId) throws Exception {
		File file = new File("/" + userId + "/" + fileId);
		ServletContext sc = request.getSession().getServletContext();
		response.reset();
		response.setContentType(sc.getMimeType(file.getName()));
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		org.springframework.util.FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

	@PostMapping("/upload")
	public ResponseEntity upload(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		InputStream inputStream = filePart.getInputStream();

//		File dir = new File ("D:/TEST/" + UUID.randomUUID());
//		dir.mkdirs();
		File f = new File("D:/Clients/hm2/" + fileName); //TODO This is for 1 day only: change path
		f.createNewFile();

		FileOutputStream outputStream =	new FileOutputStream(f);

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}

		outputStream.close();

		return ResponseEntity.ok().body("It's saved... somewhere");
	}

}
