package com.iwi.iwms.api.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.login.domain.Login;
import com.iwi.iwms.api.login.domain.LoginInfo;
import com.iwi.iwms.api.login.domain.Reissue;
import com.iwi.iwms.api.login.domain.ReissueInfo;
import com.iwi.iwms.api.login.domain.TokenInfo;
import com.iwi.iwms.api.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Login", description = "IWMS 로그인 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.path}")
public class LoginController {
	
	private final LoginService loginService;
	
	@Operation(summary = "로그인", description = "사용자 로그인")
	@PostMapping(value = "/login")
	public ResponseEntity<ApiResponse<TokenInfo>> login(HttpServletRequest request
			, @Valid Login login) {
		
		login.setLoginIp(request.getRemoteAddr());
		TokenInfo token = loginService.login(login);
		
		return ResponseEntity.ok(ApiResponse.<TokenInfo>builder()
				.data(token)
				.build());
	}
	
	@Operation(summary = "토큰 재발급", description = "토큰 재발급")
	@PostMapping(value = "/reissue")
	public ResponseEntity<ApiResponse<ReissueInfo>> reissue(HttpServletRequest request
			, @Valid Reissue reissue) {
		
		reissue.setLoginIp(request.getRemoteAddr());
		ReissueInfo token = loginService.reissue(reissue);
		
		return ResponseEntity.ok(ApiResponse.<ReissueInfo>builder()
				.data(token)
				.build());
	}
	
	@Operation(summary = "로그아웃", description = "로그아웃")
	@GetMapping(value = "/logout")
	public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo) {
		
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
}
