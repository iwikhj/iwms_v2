package com.iwi.iwms.api.user.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.domain.UserProjInfo;
import com.iwi.iwms.api.user.domain.UserPwd;
import com.iwi.iwms.api.user.domain.UserSiteInfo;
import com.iwi.iwms.api.user.domain.UserUpdate;
import com.iwi.iwms.api.user.service.UserService;
import com.iwi.iwms.utils.Pagination;
import com.iwi.iwms.utils.PredicateMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "User", description = "IWMS 사용자 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.root}/${app.version}/users")
public class UserController {
	
	private final UserService userService;
	
	@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
	@Operation(summary = "사용자 목록", description = "사용자 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<UserInfo>>> listUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "userNm", required = false) String userNm
			, @RequestParam(value = "compNm", required = false) String compNm
			, @RequestParam(value = "busiRollCd", required = false) String busiRollCd
			, @RequestParam(value = "useYn", required = false) String useYn) {
		
		Map<String, Object> map = PredicateMap.make(request, loginUserInfo);
		map.put("pagination", new Pagination(page, limit, userService.countUser(map)));
		List<UserInfo> userList = userService.listUser(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<UserInfo>>builder()
				.data(userList)
				.query(map)
				.build());
	}
	
	@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "사용자 상세 정보", description = "사용자 상세 정보")
    @GetMapping(value = "/{userSeq}")
    public ResponseEntity<ApiResponse<UserInfo>> getUserBySeq(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @PathVariable long userSeq) {
    	
    	UserInfo userInfo = userService.getUserBySeq(userSeq, loginUserInfo.getUserSeq());
    	
		return ResponseEntity.ok(ApiResponse.<UserInfo>builder()
				.data(userInfo)
				.build());
    }
	
	@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "사용자 등록", description = "사용자 등록<br/> - 신규 사용자등록 시 비밀번호는 아이디와 동일합니다")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertUser(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @ModelAttribute @Valid User user) {
    	
    	userService.insertUser(user.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
	@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "사용자 수정", description = "사용자 수정")
	@PutMapping(value = "/{userSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @PathVariable long userSeq
			, @ModelAttribute @Valid UserUpdate userUpdate) {
    	
    	boolean result = userService.updateUser(userUpdate.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
	@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "사용자 삭제", description = "사용자 삭제")
	@DeleteMapping(value = "/{userSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @PathVariable long userSeq
			, @Parameter(hidden = true) User user) {
    	
    	boolean result = userService.deleteUser(user.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
	@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "사용자 아이디 중복 확인", description = "사용자 아이디 중복 확인. true: 중복된 아이디. 사용 불가, false: 사용 가능")
    @GetMapping(value = "/exists/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> checkExistsUserId(HttpServletRequest request
    		, @PathVariable String userId) {
    	
    	boolean result = userService.checkExistsUserId(userId);
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
    }
    
    @Operation(summary = "내 정보", description = "내 정보")
    @GetMapping(value = "/me")
    public ResponseEntity<ApiResponse<UserInfo>> getMe(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {

    	UserInfo user = userService.getUserBySeq(loginUserInfo.getUserSeq(), loginUserInfo.getUserSeq());
    	
		return ResponseEntity.ok(ApiResponse.<UserInfo>builder()
				.data(user)
				.build());
    }
    
    @Operation(summary = "내 비밀번호 변경", description = "내 비밀번호 변경")
	@PatchMapping(value = "/password-change", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> passwordChange(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @ModelAttribute @Valid UserPwd userPwd) {
    	
    	userPwd.setUserSeq(loginUserInfo.getUserSeq());
    	userPwd.setPwdResetYn("N");
    	boolean result = userService.passwordChange(userPwd.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "사용자 비밀번호 초기화", description = "사용자 비밀번호 초기화")
    @PatchMapping(value = "/password-reset", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> passwordReset(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @ModelAttribute @Valid UserPwd userPwd) {
    	
    	userPwd.setPwdResetYn("Y");
    	boolean result = userService.passwordReset(userPwd.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
	@Operation(summary = "사용자별 이용 가능한 프로젝트 목록", description = "사용자별 이용 가능한 프로젝트 목록")
	@GetMapping(value = "/{userSeq}/projects")
	public ResponseEntity<ApiResponse<List<UserProjInfo>>> listProjByUserSeq(HttpServletRequest request
			, @PathVariable long userSeq) {
		
    	List<UserProjInfo> listProj = userService.listProjByUserSeq(userSeq);
		
		return ResponseEntity.ok(ApiResponse.<List<UserProjInfo>>builder()
				.data(listProj)
				.build());
	}
	
	@Operation(summary = "사용자별 이용 가능한 사이트 목록", description = "사용자별 이용 가능한 사이트 목록")
	@GetMapping(value = "/{userSeq}/sites")
	public ResponseEntity<ApiResponse<List<UserSiteInfo>>> listSiteByUserSeq(HttpServletRequest request
			, @PathVariable long userSeq) {
		
    	List<UserSiteInfo> listSite = userService.listSiteByUserSeq(userSeq);
		
		return ResponseEntity.ok(ApiResponse.<List<UserSiteInfo>>builder()
				.data(listSite)
				.build());
	}
}
