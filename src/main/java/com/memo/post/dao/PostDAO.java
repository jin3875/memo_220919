package com.memo.post.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	
	public List<Map<String, Object>> selectPostListTEST();
	
	// 글 추가
	public int insertPost(
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	// 글 수정
	public void updatePostByPostIdUserId(
			@Param("postId") int postId,
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	// 글 삭제
	public int deletePostByPostIdUserId(
			@Param("postId") int postId,
			@Param("userId") int userId);
	
	// userId 글 목록
	public List<Post> selectPostListByUserId(
			@Param("userId") int userId,
			@Param("direction") String direction,
			@Param("standardId") Integer standardId,
			@Param("limit") int limit);
	
	// 가장 크거나 가장 작은 postId
	public int selectPostIdByUserIdSort(
			@Param("userId") int userId,
			@Param("sort") String sort);
	
	// userId의 postId 글
	public Post selectPostByPostIdUserId(
			@Param("postId") int postId,
			@Param("userId") int userId);

}
