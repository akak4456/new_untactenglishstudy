package com.untact.service.filesystemmanipulate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.java.Log;

@Service
@Log
public class FileSystemManipulateServiceImpl implements FileSystemManipulateService {
	@Value("${spring.servlet.multipart.location}")
	private String rootPath;
	
	private Path getPath(String fileName) {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int date = now.get(Calendar.DATE);
		return Paths.get(rootPath, String.valueOf(year), String.valueOf(month), String.valueOf(date), fileName);
	}
	
	@Override
	public String saveFile(MultipartFile file) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
		Path path = getPath(file.getOriginalFilename());
		if (Files.notExists(path.getParent())) {
			Files.createDirectories(path.getParent());
		}
		file.transferTo(path);
		return path.subpath(path.getNameCount() - 4, path.getNameCount()).toString().replace("\\", "/");
	}

	@Override
	public Resource getFile(String year, String month, String date, String name) throws IOException{
		Path path = Paths.get(rootPath, year, month, date, name);
		String contentType = Files.probeContentType(path);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		return new InputStreamResource(Files.newInputStream(path));
	}

}
