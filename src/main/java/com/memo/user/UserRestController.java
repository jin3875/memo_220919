package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {
	
	@Autowired
	private UserBO userBO;
	
	/**
	 * 아이디 중복확인 API
	 * @param loginId
	 * @return
	 */
	@GetMapping("/is_duplicated_id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId
	) {
		Map<String, Object> result = new HashMap<>();
		
		boolean isDuplicated = false;
		
		try {
			// loginId 존재 유무
			isDuplicated = userBO.existLoginId(loginId);
		} catch(Exception e) {
			result.put("code", 500);
			result.put("errorMessage", "중복확인에 실패했습니다.");
			return result;
		}
		
		if (isDuplicated) {
			// 중복
			result.put("code", 1);
			result.put("result", true);
		} else {
			// 사용 가능
			result.put("code", 1);
			result.put("result", false);
		}
		
		return result;
	}
	
	/**
	 * 회원가입 API
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign_up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email
	) {
		Map<String, Object> result = new HashMap<>();
		
		// 비밀번호 암호화
		String hashedPassword = EncryptUtils.md5(password);
		
		// 유저 추가
		userBO.addUser(loginId, hashedPassword, name, email);
		result.put("code", 1);
		result.put("result", "성공");
		
		return result;
	}
	
	/**
	 * 로그인 API
	 * @param loginId
	 * @param password
	 * @param session
	 * @return
	 */
	@PostMapping("/sign_in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpSession session
	) {
		Map<String, Object> result = new HashMap<>();
		
		// 비밀번호 암호화
		String hashedPassword = EncryptUtils.md5(password);
		
		// 유저 검색
		User user = userBO.getUserByLoginIdPassword(loginId, hashedPassword);
		
		if (user != null) {
			// 로그인
			result.put("code", 1);
			result.put("result", "성공");
			
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());
		} else {
			// 로그인 실패
			result.put("code", 500);
			result.put("errorMessage", "존재하지 않는 사용자입니다");
		}
		
		return result;
	}

}
