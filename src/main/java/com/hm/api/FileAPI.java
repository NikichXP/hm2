package com.hm.api;

import com.hm.entity.User;
import com.hm.model.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.val;
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

	@RequestMapping("/get")
	public void getFile(HttpServletResponse response, HttpServletRequest request, @RequestParam("file") String filePath) throws Exception {
		val file = new File(System.getProperty("user.dir") + "/src/main/resources/files/" + filePath);

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

	@RequestMapping("/get/{userId}")
	public void getFile(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") String userId,
	                    @RequestParam("fileId") String fileId) throws Exception {

		getFile(response, request, userId + "/" + fileId);
	}

	@RequestMapping("/get/{userId}/{fileId}/{ext}") //alternate mapping
	public void getFile2(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") String userId,
	                     @PathVariable("fileId") String fileId, @PathVariable("ext") String ext) throws Exception {
		System.out.println(fileId);
		getFile(response, request, userId + "/" + fileId + "." + ext);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
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
		String path = System.getProperty("user.dir") + "/src/main/resources/files/" + userId + "/";
		File dir = new File(path);
		dir.mkdirs();
		File f = new File(path + fileName);
		System.out.println(f.getAbsolutePath());
		try {
			f.createNewFile();
		} catch (Exception e) {
			System.out.println("Error creating " + f.getAbsolutePath());
			f.getParentFile().mkdirs();
			f.createNewFile();
		}

		FileOutputStream outputStream = new FileOutputStream(f);

		int read = 0;
		byte[] bytes = new byte[4096];

		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}

		outputStream.close();

		return ResponseEntity.ok().body("Save success: " + f.getAbsolutePath());
	}

	//TODO Test methods below

	@RequestMapping("/dir")
	public ResponseEntity dir(@RequestParam(value = "dir", required = false) String dir) {
		if (dir == null) {
			return ResponseEntity.ok().body(File.listRoots());
		} else {
			return ResponseEntity.ok().body(new File(dir).list());
		}
	}

	@RequestMapping("/local")
	public ResponseEntity local() {
		return ResponseEntity.ok().body(System.getProperty("user.dir"));
	}

}
