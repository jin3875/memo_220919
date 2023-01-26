<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1 class="mb-3">글 상세/수정</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요" value="${post.subject}">
		<textarea id="content" class="form-control" rows="15" placeholder="내용을 입력하세요">${post.content}</textarea>
		
		<%-- 이미지가 있을 때만 이미지 영역 추가 --%>
		<c:if test="${not empty post.imagePath}">
			<div class="mt-3">
				<img src="${post.imagePath}" alt="업로드 이미지" width="300">
			</div>
		</c:if>
		
		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg,.jpeg,.png,.gif">
		</div>
		
		<div class="d-flex justify-content-between">
			<button type="button" id="postDeleteBtn" class="btn btn-secondary" data-post-id="${post.id}">삭제</button>
			<div class="d-flex">
				<a href="/post/post_list_view" id="postListBtn" class="btn btn-dark mr-3">목록으로</a>
				<button type="button" id="postUpdateBtn" class="btn btn-info" data-post-id="${post.id}">수정</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {
		// 수정 버튼 클릭
		$('#postUpdateBtn').on('click', function() {
			let postId = $(this).data('post-id');
			let subject = $('#subject').val().trim();
			let content = $('#content').val();
			let file = $('#file').val();
			
			if (subject == '') {
				alert("제목을 입력하세요");
				return;
			}
			
			// 파일이 업로드된 경우 확장자 체크
			if (file != '') {
				let ext = file.split(".").pop().toLowerCase();
				
				if ($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) {
					alert("이미지 파일만 업로드 할 수 있습니다");
					$('#file').val(''); // 파일 비우기
					return;
				}
			}
			
			let formData = new FormData();
			formData.append("postId", postId);
			formData.append("subject", subject);
			formData.append("content", content);
			formData.append("file", $('#file')[0].files[0]);
			
			$.ajax({
				type:"PUT"
				, url:"/post/update"
				, data:formData
				// 파일 업로드를 위한 필수 설정
				, enctype:"multipart/form-data"
				, processData:false
				, contentType:false
				
				, success:function(data) {
					if (data.code == 1) {
						alert("메모가 수정되었습니다");
						location.reload();
					} else {
						alert(data.errorMessage);
					}
				}
				, error:function(e) {
					alert("메모 수정에 실패했습니다");
				}
			});
		});
		
		// 삭제 버튼 클릭
		$('#postDeleteBtn').on('click', function() {
			let postId = $(this).data('post-id');
			
			$.ajax({
				type:"DELETE"
				, url:"/post/delete"
				, data:{"postId":postId}
				
				, success:function(data) {
					if (data.result == "성공") {
						alert("삭제되었습니다");
						location.href="/post/post_list_view";
					} else {
						alert(data.errorMessage);
					}
				}
				, error:function(e) {
					alert("메모 삭제에 실패했습니다");
				}
			});
		});
	});
</script>