package com.memo.post.bo;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final int POST_MAX_SIZE = 3;
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// 글 추가
	public int addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		String imagePath = null;
		
		if (file != null) {
			imagePath = fileManagerService.saveFile(userLoginId, file);
		}
		
		return postDAO.insertPost(userId, subject, content, imagePath);
	}
	
	// 글 수정
	public void updatePost(int userId, String userLoginId, int postId, String subject, String content, MultipartFile file) {
		// 기존 글
		Post post = getPostByPostIdUserId(postId, userId);
		
		if (post == null) {
			logger.warn("[update post] 수정할 메모가 존재하지 않습니다. postId : {}, userId : {}", postId, userId);
			return;
		}
		
		String imagePath = null;
		
		if (file != null) {
			imagePath = fileManagerService.saveFile(userLoginId, file);
			
			// 기존 이미지 삭제
			if (imagePath != null && post.getImagePath() != null) {
				fileManagerService.deleteFile(post.getImagePath());
			}
		}
		
		postDAO.updatePostByPostIdUserId(postId, userId, subject, content, imagePath);
	}
	
	// 글 삭제
	public int deletePostByPostIdUserId(int postId, int userId) {
		Post post = getPostByPostIdUserId(postId, userId);
		
		if (post == null) {
			logger.warn("[글 삭제] post is null. postId : {}, userId : {}", postId, userId);
			return 0;
		}
		
		// 이미지 삭제
		if (post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath());
		}
		
		return postDAO.deletePostByPostIdUserId(postId, userId);
	}
	
	// userId 글 목록
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId) {
		String direction = null;
		Integer standardId = null;
		
		if (prevId != null) {
			direction = "prev";
			standardId = prevId;
			
			List<Post> postList = postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
			Collections.reverse(postList);
			
			return postList;
		} else if (nextId != null) {
			direction = "next";
			standardId = nextId;
		}
		
		return postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
	}
	
	// 이전 방향 마지막 페이지인지 boolean
	public boolean isPrevLastPage(int prevId, int userId) {
		// 가장 큰 postId
		int maxPostId = postDAO.selectPostIdByUserIdSort(userId, "DESC");
		
		return maxPostId == prevId ? true : false;
	}
	
	// 다음 방향 마지막 페이지인지 boolean
	public boolean isNextLastPage(int nextId, int userId) {
		// 가장 작은 postId
		int maxPostId = postDAO.selectPostIdByUserIdSort(userId, "ASC");
		
		return maxPostId == nextId ? true : false;
	}
	
	// userId의 postId 글
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postDAO.selectPostByPostIdUserId(postId, userId);
	}

}
