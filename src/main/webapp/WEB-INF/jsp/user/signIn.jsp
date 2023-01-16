<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center">
	<div class="login-box">
		<h1 class="mt-3 mb-3">로그인</h1>
		<form id="loginForm" action="/user/sign_in" method="post">
			<div class="input-group mb-3">
				<div class="input-group-prepend">
					<span class="input-group-text">ID</span>
				</div>
				<input type="text" class="form-control" id="loginId" name="loginId">
			</div>
			
			<div class="input-group mb-3">
				<div class="input-group-prepend">
					<span class="input-group-text">PW</span>
				</div>
				<input type="password" class="form-control" id="password" name="password">
			</div>
			
			<input type="submit" class="btn btn-block btn-primary" value="로그인">
			<a class="btn btn-block btn-dark" href="/user/sign_up_view">회원가입</a>
		</form>
	</div>
</div>

<script>
	$(document).ready(function() {
		$('#loginForm').on('submit', function(e) {
			// submit 기능 중단
			e.preventDefault();
			
			// validation
			let loginId = $('input[name=loginId]').val().trim();
			let password = $('#password').val();
			
			if (loginId == '') {
				alert("아이디를 입력해주세요");
				return false;
			}
			
			if (password == '') {
				alert("비밀번호를 입력해주세요");
				return false;
			}
			
			// ajax
			let url = $(this).attr('action');
			let params = $(this).serialize();
			
			$.post(url, params) // request
			.done(function(data) { // response
				if (data.code == 1) { // 성공
					location.href = "/post/post_list_view"; // 글 목록으로 이동
				} else { // 실패
					alert(data.errorMessage);
				}
			});
		});
	});
</script>