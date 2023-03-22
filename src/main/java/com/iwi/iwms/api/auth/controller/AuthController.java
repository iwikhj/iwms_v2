package com.iwi.iwms.api.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.auth.domain.Auth;
import com.iwi.iwms.api.auth.domain.AuthInfo;
import com.iwi.iwms.api.auth.service.AuthService;
import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Auth", description = "IWMS 권한 관리")
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
@RequestMapping("${app.root}/${app.version}/auths")
public class AuthController {
	
	private final AuthService authService;

	@Operation(summary = "권한 목록", description = "권한 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<AuthInfo>>> listAuth(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, authService.countAuth(map)));
		
		List<AuthInfo> authList = authService.listAuth(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<AuthInfo>>builder()
				.data(authList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "권한 정보", description = "권한 정보")
    @GetMapping(value = "/{authSeq}")
    public ResponseEntity<ApiResponse<AuthInfo>> getAuthBySeq(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @PathVariable long authSeq) {
    	
    	AuthInfo authInfo = authService.getAuthBySeq(authSeq, loginUserInfo.getUserSeq());
    	
		return ResponseEntity.ok(ApiResponse.<AuthInfo>builder()
				.data(authInfo)
				.build());
    }
    
    @Operation(summary = "권한 수정", description = "권한 수정")
	@PutMapping(value = "/{authSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateAuth(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @PathVariable long authSeq
			, @ModelAttribute @Valid Auth auth) {
    	
    	boolean result = authService.updateAuth(auth.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
}
