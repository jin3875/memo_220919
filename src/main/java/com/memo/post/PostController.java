package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글 목록 화면
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/post_list_view")
	public String postListView(Model model, HttpSession session) {
		Integer userId = (Integer)session.getAttribute("userId");
		
		if (userId == null) {
			return "redirect:/user/sign_in_view";
		}
		
		// userId의 글 목록
		List<Post> postList = postBO.getPostListByUserId(userId);
		model.addAttribute("postList", postList);
		
		model.addAttribute("viewName", "post/postList");
		return "template/layout";
	}
	
	/**
	 * 글 작성 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/post_create_view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName", "post/postCreate");
		return "template/layout";
	}
	
	/**
	 * 글 상세/수정 화면
	 * @param postId
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/post_detail_view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			HttpSession session,
			Model model
	) {
		Integer userId = (Integer)session.getAttribute("userId");
		
		if (userId == null) {
			return "redirect:/user/sign_in_view";
		}
		
		// userId의 postId 글
		Post post = postBO.getPostByPostIdUserId(postId, userId);
		model.addAttribute("post", post);
		
		model.addAttribute("viewName", "post/postDetail");
		return "template/layout";
	}

}
