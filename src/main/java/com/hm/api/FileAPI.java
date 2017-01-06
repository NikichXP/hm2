package com.hm.api;

import com.hm.entity.User;
import com.hm.model.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileAPI {

	@Autowired
	AuthController auth;

	@RequestMapping("/get/{userId}")
	public void getFile(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") String userId,
	                     @RequestParam("fileId") String fileId) throws Exception {

		File file;
		file = new File("/usr/local/" + userId + "/" + fileId);
		if (!file.exists()) {
			file = new File("D:/usr/local/" + userId + "/" + fileId);
		}

		if (!file.exists()) {
			response.getWriter().write("File not found");
			return;
		}


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
		String fileExt = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		fileExt = fileExt.substring(fileExt.lastIndexOf("."));
		String fileName = UUID.randomUUID().toString() + fileExt;
		InputStream inputStream = filePart.getInputStream();

		User u = auth.getUser(request.getParameter("token"));
		if (u == null) {
			return ResponseEntity.status(403).body("Need authorization");
		}
		String userId = u.getId();


		File dir;
		try {
			dir = new File("/usr/local/" + userId + "/");
			dir.mkdirs();
		} catch (Exception e) {
			dir = new File ("D:/Work/hm2-files/" + userId + "/");
			dir.mkdirs();
		}
		File f = new File(dir.getAbsolutePath() + "/" + fileName);
		f.createNewFile();

		FileOutputStream outputStream =	new FileOutputStream(f);

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}

		outputStream.close();

		return ResponseEntity.ok().body("Save success: " + f.getAbsolutePath());
	}

	@RequestMapping("/dir")
	public ResponseEntity dir (@RequestParam(value = "dir", required = false) String dir) {
		if (dir == null) {
			return ResponseEntity.ok().body(File.listRoots());
		} else {
			return ResponseEntity.ok().body(new File(dir).list());
		}
	}

	@RequestMapping("/local")
	public ResponseEntity local () {
		return ResponseEntity.ok().body(System.getProperty("user.dir"));
	}

}
