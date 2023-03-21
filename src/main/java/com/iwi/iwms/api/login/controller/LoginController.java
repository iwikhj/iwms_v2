package com.iwi.iwms.api.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.login.domain.Login;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.login.domain.Reissue;
import com.iwi.iwms.api.login.service.LoginService;
import com.iwi.iwms.config.security.auth.ReissueResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Login", description = "IWMS 로그인 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.root}/${app.version}")
public class LoginController {
	
	private final LoginService loginService;
	
	@Operation(summary = "로그인", description = "사용자 로그인")
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<AccessTokenResponse>> login(HttpServletRequest request
			, @ModelAttribute @Valid Login login) {
		
		login.setLoginIp(request.getRemoteAddr());
		AccessTokenResponse token = loginService.login(login);
		
		return ResponseEntity.ok(ApiResponse.<AccessTokenResponse>builder()
				.request(request)
				.data(token)
				.build());
	}
	
	@Operation(summary = "로그아웃", description = "로그아웃")
	@GetMapping(value = "/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
		
    	return ResponseEntity.ok()
    			.build();
	}
	
	@Operation(summary = "토큰 재발급", description = "토큰 재발급")
	@PostMapping(value = "/reissue", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<ReissueResponse>> reissue(HttpServletRequest request
			, @ModelAttribute @Valid Reissue reissue) {
		
		reissue.setLoginIp(request.getRemoteAddr());
		ReissueResponse token = loginService.reissue(reissue);
		
		return ResponseEntity.ok(ApiResponse.<ReissueResponse>builder()
				.request(request)
				.data(token)
				.build());
	}

}
