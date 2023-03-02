package com.iwi.iwms.api.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.user.domain.PasswordChange;
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.domain.UserUpdate;
import com.iwi.iwms.api.user.service.UserService;
import com.iwi.iwms.utils.CookieUtil;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "User", description = "IWMS 사용자 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
public class UserController {
	
	private final UserService userService;
	
	@Operation(summary = "사용자 목록", description = "사용자 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<UserInfo>>> listUser(HttpServletRequest request
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, userService.countUser(map)));
		
		List<UserInfo> userList = userService.listUser(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<UserInfo>>builder()
				.request(request)
				.data(userList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "사용자 정보", description = "사용자 정보")
    @GetMapping(value = "/{userSeq}")
    public ResponseEntity<ApiResponse<UserInfo>> getUserBySeq(HttpServletRequest request
    		, @PathVariable long userSeq) {
    	
    	UserInfo user = userService.getUserBySeq(userSeq);
    	
		return ResponseEntity.ok(ApiResponse.<UserInfo>builder()
				.request(request)
				.data(user)
				.build());
    }
    
    @Operation(summary = "사용자 등록", description = "사용자 등록<br/> - 신규 사용자등록 시 비밀번호는 아이디와 동일합니다")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertUser(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @ModelAttribute @Valid User user) {
    	
    	userService.insertUser(user.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "사용자 수정", description = "사용자 수정")
	@PutMapping(value = "/{userSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @PathVariable long userSeq
			, @ModelAttribute @Valid UserUpdate userUpdate) {
    	
    	boolean result = userService.updateUser(userUpdate.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "사용자 삭제", description = "사용자 삭제")
	@DeleteMapping(value = "/{userSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @PathVariable long userSeq
			, @Parameter(hidden = true) User user) {
    	
    	boolean result = userService.deleteUser(user.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "사용자 아이디 중복 확인", description = "사용자 아이디 중복 확인. true: 중복된 아이디. 사용 불가, false: 사용 가능")
    @GetMapping(value = "/exists/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> checkDuplicateUserId(HttpServletRequest request
    		, @PathVariable String userId) {
    	
    	boolean result = userService.checkDuplicateUserId(userId);
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
    }
    
    @Operation(summary = "내 정보", description = "내 정보")
    @GetMapping(value = "/me")
    public ResponseEntity<ApiResponse<UserInfo>> getMe(HttpServletRequest request, HttpServletResponse response
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {

    	UserInfo user = userService.getUserBySeq(loginUserInfo.getUserSeq());
    	
		return ResponseEntity.ok(ApiResponse.<UserInfo>builder()
				.request(request)
				.data(user)
				.build());
    }
    
    @Operation(summary = "내 비밀번호 변경", description = "내 비밀번호 변경")
	@PatchMapping(value = "/changePassword", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> changePassword(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @ModelAttribute @Valid PasswordChange passwordChange) {
    	
    	boolean result = userService.changePassword(passwordChange.ofChange(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "사용자 비밀번호 초기화", description = "사용자 비밀번호 초기화")
    @PatchMapping(value = "/{userSeq}/resetPassword")
	public ResponseEntity<ApiResponse<Boolean>> resetPassword(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @PathVariable long userSeq
			, @Parameter(hidden = true) PasswordChange passwordChange) {
    	
    	boolean result = userService.resetPassword(passwordChange.ofReset(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
}
