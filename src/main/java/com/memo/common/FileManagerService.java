package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component // 일반적인 스프링 빈
public class FileManagerService {
	
	// 실제 이미지가 저장될 경로(서버) -> 경로 뒤에 "/" 붙이기
	// 세팅 1)
	public static final String FILE_UPLOAD_PATH = "D:\\kimjinyoung\\6_spring_project\\memo\\workspace\\images/";
	// 세팅 2)
//	public static final String FILE_UPLOAD_PATH = "C:\\Users\\jinyo\\Desktop\\웹개발\\6_spring_project\\memo\\workspace\\images/";
	
	// input : MultipartFile, userLoginId
	// output : image path
	public String saveFile(String userLoginId, MultipartFile file) {
		// 파일 디렉토리 예) 유저아이디_1620451673/파일이름.확장자
		String directoryName = userLoginId + "_" + System.currentTimeMillis() + "/"; // 유저아이디_1620451673/
		String filePath = FILE_UPLOAD_PATH + directoryName; // D:\\kimjinyoung\\6_spring_project\\memo\\workspace\\images/유저아이디_1620451673/
		
		File directory = new File(filePath);
		if (directory.mkdir() == false) {
			return null; // 폴더 만드는 데 실패시 이미지패스 null
		}
		
		// 파일 업로드 : byte 단위로 업로드된다.
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(filePath + file.getOriginalFilename()); // OriginalFilename : 사용자가 올린 파일명 -> 한글이름 파일 X (영어 & 숫자만)
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// 파일 업로드 성공했으면 이미지 url path를 리턴한다.
		// http://localhost/images/유저아이디_1620451673/파일이름.확장자
		return "/images/" + directoryName + file.getOriginalFilename();
	}

}
