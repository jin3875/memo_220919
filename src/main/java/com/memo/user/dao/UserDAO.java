package com.memo.user.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.user.model.User;

@Repository
public interface UserDAO {
	
	// loginId 존재 유무
	public boolean existLoginId(String loginId);
	
	// 유저 추가
	public void insertUser(
			@Param("loginId") String loginId,
			@Param("password") String password,
			@Param("name") String name,
			@Param("email") String email);
	
	// 유저 검색
	public User selectUserByLoginIdPassword(
			@Param("loginId") String loginId,
			@Param("password") String password);

}
