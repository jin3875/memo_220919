<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1 class="mb-3">글 작성</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요">
		<textarea id="content" class="form-control" rows="15" placeholder="내용을 입력하세요"></textarea>
		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg,.jpeg,.png,.gif">
		</div>
		
		<div class="d-flex justify-content-between">
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
			<div class="d-flex">
				<button type="button" id="clearBtn" class="btn btn-secondary mr-3">모두 지우기</button>
				<button type="button" id="postCreateBtn" class="btn btn-info">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {
		// 목록 버튼 클릭 -> 글 목록
		$('#postListBtn').on('click', function() {
			location.href = "/post/post_list_view";
		});
		
		// 모두 지우기 버튼 클릭 -> 제목, 글 내용 지우기
		$('#clearBtn').on('click', function() {
			$('#subject').val('');
			$('#content').val('');
		});
		
		// 글 저장
		$('#postCreateBtn').on('click', function() {
			let subject = $('#subject').val().trim();
			let content = $('#content').val();
			
			if (subject == '') {
				alert("제목을 입력하세요");
				return;
			}
			
			let file = $('#file').val();
			// alert(file); // C:\fakepath\파일이름.확장자
			
			// 파일이 업로드된 경우에만 확장자 체크
			if (file != '') {
				// alert(file.split(".").pop().toLowerCase());
				let ext = file.split(".").pop().toLowerCase();
				
				if ($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) {
					alert("이미지 파일만 업로드 할 수 있습니다");
					$('#file').val(''); // 파일 비우기
					return;
				}
			}
			
			// 서버 - AJAX
			
			// 이미지를 업로드할 때는 form태그가 있어야 한다. (자바스크립트에서 만듦)
			// append로 넣는 값은 form태그의 name으로 넣는 것과 같다. (request parameter)
			let formData = new FormData();
			formData.append("subject", subject);
			formData.append("content", content);
			formData.append("file", $('#file')[0].files[0]);
			
			// AJAX 통신으로 formData에 있는 데이터 전송
			$.ajax({
				// request
				type:"POST"
				, url:"/post/create"
				, data:formData // form 객체를 통째로
				// 파일 업로드를 위한 필수 설정
				, enctype:"multipart/form-data"
				, processData:false
				, contentType:false
				
				// response
				, success:function(data) {
					if (data.code == 1) {
						// 성공
						alert("메모가 저장되었습니다");
						location.href = "/post/post_list_view";
					} else {
						// 실패
						alert(data.errorMessage);
					}
				}
				, error:function(e) {
					alert("메모 저장에 실패했습니다");
				}
			});
		});
	});
</script>