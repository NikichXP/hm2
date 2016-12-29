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

		/*
		 * TODO Below only 1-day code, delete after completing labs
		 */

		StringBuilder ret = new StringBuilder();
		if (fileName.endsWith(".java")) {
//			ret.append(runProcess("cmd /c start cmd.exe /K \"cd C:\\Test && javac AppLoader.java && java AppLoader\""));
			ret.append(runProcess("javac " + fileName));
			ret.append(runProcess("java " + fileName.substring(fileName.length() - 5)));
		} else if (fileName.endsWith(".class")){
			ret = runProcess("java " + fileName.substring(fileName.length() - 6));
		}
		f.delete();
		return ResponseEntity.ok().body(ret);
	}

	private static void printLines(InputStream ins, StringBuilder ret) throws Exception {
		String line = null;
		BufferedReader in = new BufferedReader(
				new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			ret.append(line + "\n");
		}
	}

	private static StringBuilder runProcess(String command) throws Exception {
		StringBuilder out = new StringBuilder();
		Process pro = Runtime.getRuntime().exec(command);
		pro.waitFor();
		printLines(pro.getInputStream(), out);
		printLines(pro.getErrorStream(), out);
//		out.append(command + " exitValue() " + pro.exitValue());
		return out;
	}

	@RequestMapping("/*")
	public ResponseEntity test () {
		return ResponseEntity.ok().body("ok!");
	}
}
