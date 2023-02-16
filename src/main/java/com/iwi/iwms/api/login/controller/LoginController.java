package com.iwi.iwms.api.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.login.domain.Login;
import com.iwi.iwms.api.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Login", description = "IWMS 로그인 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/login")
public class LoginController {
	
	private final LoginService loginService;
	
	@Operation(summary = "로그인", description = "SSO 사용자 로그인")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<AccessTokenResponse>> login(HttpServletRequest request, HttpServletResponse response
			, @ModelAttribute @Valid Login login) {
		
		AccessTokenResponse accessTokenResponse = loginService.login(request, response, login);
		
		return ResponseEntity.ok(ApiResponse.<AccessTokenResponse>builder()
				.request(request)
				.data(accessTokenResponse)
				.build());
	}

}
