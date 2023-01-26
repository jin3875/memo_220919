package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component // 일반적인 스프링 빈
public class FileManagerService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 실제 이미지가 저장될 경로(서버)
	// 세팅 1)
	public static final String FILE_UPLOAD_PATH = "D:\\kimjinyoung\\6_spring_project\\memo\\workspace\\images/";
	// 세팅 2)
//	public static final String FILE_UPLOAD_PATH = "C:\\Users\\jinyo\\Desktop\\웹개발\\6_spring_project\\memo\\workspace\\images/";
	
	// input : MultipartFile, userLoginId / output : imagePath
	public String saveFile(String userLoginId, MultipartFile file) {
		String directoryName = userLoginId + "_" + System.currentTimeMillis() + "/"; // 유저아이디_1620451673/
		String filePath = FILE_UPLOAD_PATH + directoryName; // D:\\kimjinyoung\\6_spring_project\\memo\\workspace\\images/유저아이디_1620451673/
		
		File directory = new File(filePath);
		
		if (directory.mkdir() == false) {
			return null; // 폴더 만드는 데 실패시 imagePath null
		}
		
		// 파일 업로드 : byte 단위로 업로드
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(filePath + file.getOriginalFilename()); // OriginalFilename : 사용자가 올린 파일명 -> 한글이름 파일 X (영어 & 숫자만)
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// 파일 업로드 성공했으면 이미지 url path를 리턴
		// http://localhost/images/유저아이디_1620451673/파일이름.확장자
		return "/images/" + directoryName + file.getOriginalFilename();
	}
	
	public void deleteFile(String imagePath) { // imagePath : /images/유저아이디_1620451673/파일이름.확장자
		// ...\\images/ + /images/... -> imagePath에 있는 겹치는 /images/ 구문 제거
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		
		if (Files.exists(path)) {
			try {
				// 이미지 삭제
				Files.delete(path);
			} catch (IOException e) {
				logger.error("[이미지 삭제] 이미지 삭제 실패. imagePath : {}", imagePath);
			}
			
			// 디렉토리(폴더) 삭제
			path = path.getParent();
			
			if (Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					logger.error("[이미지 삭제] 디렉토리 삭제 실패. imagePath : {}", imagePath);
				}
			}
		}
	}

}
