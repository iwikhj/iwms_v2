package com.iwi.iwms.api.menu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.code.domain.CodeInfo;
import com.iwi.iwms.api.code.service.CodeService;
import com.iwi.iwms.api.common.response.PageResponse;
import com.iwi.iwms.api.login.domain.LoginInfo;
import com.iwi.iwms.api.user.domain.UserSiteInfo;
import com.iwi.iwms.api.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Page", description = "IWMS 페이지 기본 정보")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.path}/pages")
public class PageController {
	
	private final CodeService codeService;
	
	private final UserService userService;
	
	/**
	 * 메뉴 홈
	 */
    @Operation(summary = "홈", description = "홈")
    @GetMapping(value = "/home")
    public ResponseEntity<PageResponse<Map<String, Object>>> home(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
	/**
	 * 메뉴 공지사항
	 */
    @Operation(summary = "공지사항", description = "공지사항 목록")
    @GetMapping(value = "/notice")
    public ResponseEntity<PageResponse<Map<String, Object>>> notice(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    @Operation(summary = "공지사항 - 상세", description = "공지사항 상세")
    @GetMapping(value = "/notice/detail")
    public ResponseEntity<PageResponse<Map<String, Object>>> noticeDetail(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
	/**
	 * 메뉴 유지보수
	 */
    @Operation(summary = "유지보수 - 유지보수 ", description = "유지보수 목록")
    @GetMapping(value = "/maintain/request")
    public ResponseEntity<PageResponse<Map<String, Object>>> maintainRequest(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();
    	
    	List<UserSiteInfo> siteList = userService.listSiteByUserSeq(loginInfo.getUserSeq());
    	List<CodeInfo> reqStatCdList =  codeService.listCodeByUpCode("REQ_STAT_CD");
    	List<CodeInfo> reqDtlStatCdList =  codeService.listCodeByUpCode("REQ_DTL_STAT_CD");
    	reqStatCdList.addAll(reqDtlStatCdList);
    	
    	List<CodeInfo> reqGbCdList =  codeService.listCodeByUpCode("REQ_GB_CD");
    	List<CodeInfo> reqTypeCdList =  codeService.listCodeByUpCode("REQ_TYPE_CD");
    	List<CodeInfo> busiRollCdList =  codeService.listCodeByUpCode("BUSI_ROLL_CD");
    	
    	data.put("siteList", siteList);
    	data.put("reqStatCdList", reqStatCdList);
    	data.put("reqGbCdList", reqGbCdList);
    	data.put("reqTypeCdList", reqTypeCdList);
    	data.put("busiRollCdList", busiRollCdList);
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    @Operation(summary = "유지보수 - 유지보수 - 상세 ", description = "유지보수 상세")
    @GetMapping(value = "/maintain/request/detail")
    public ResponseEntity<PageResponse<Map<String, Object>>> maintainRequestDetail(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo
    		, @RequestParam(value = "rSeq", required = true) long reqSeq
    		, @RequestParam(value = "dSeq", required = false) Long reqDtlSeq) {
    	
    	Map<String, Object> data = new HashMap<>();
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    @Operation(summary = "유지보수 - 기능개발", description = "유지보수 기능개발")
    @GetMapping(value = "/maintain/develop")
    public ResponseEntity<PageResponse<Map<String, Object>>> maintainDevelop(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    @Operation(summary = "유지보수 - 일정관리", description = "유지보수 일정관리")
    @GetMapping(value = "/maintain/schedule")
    public ResponseEntity<PageResponse<Map<String, Object>>> maintainSchedule(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    @Operation(summary = "유지보수 - 현황", description = "유지보수 현황")
    @GetMapping(value = "/maintain/statistics")
    public ResponseEntity<PageResponse<Map<String, Object>>> maintainStatistics(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {

    	Map<String, Object> data = new HashMap<>();	
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
	/**
	 * 메뉴 프로젝트
	 */    
    @Operation(summary = "프로젝트", description = "프로젝트 목록")
    @GetMapping(value = "/project")
    public ResponseEntity<PageResponse<Map<String, Object>>> project(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();	
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
	/**
	 * 메뉴 이력관리
	 */
    @Operation(summary = "이력관리", description = "이력관리")
    @GetMapping(value = "/history")
    public ResponseEntity<PageResponse<Map<String, Object>>> history(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();	
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    
	/**
	 * 메뉴 배포관리
	 */
    @Operation(summary = "배포관리", description = "배포관리")
    @GetMapping(value = "/deploy")
    public ResponseEntity<PageResponse<Map<String, Object>>> deploy(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();	
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    
	/**
	 * 메뉴 시스템관리
	 */
    @PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "시스템관리 - 사용자", description = "사용자 목록")
    @GetMapping(value = "/system/user")
    public ResponseEntity<PageResponse<Map<String, Object>>> systemUser(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {

    	Map<String, Object> data = new HashMap<>();	    
    	
    	List<CodeInfo> busiRollCdList =  codeService.listCodeByUpCode("BUSI_ROLL_CD");
    	
    	data.put("busiRollCdList", busiRollCdList);
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    @PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "시스템관리 - 소속 ", description = "소속 목록")
    @GetMapping(value = "/system/company")
    public ResponseEntity<PageResponse<Map<String, Object>>> systemCompany(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();
    	
    	List<CodeInfo> compGbCdList =  codeService.listCodeByUpCode("COMP_GB_CD");
    	
    	data.put("compGbCdList", compGbCdList);
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    @PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "시스템관리 - 프로젝트 ", description = "프로젝트 목록")
    @GetMapping(value = "/system/project")
    public ResponseEntity<PageResponse<Map<String, Object>>> systemProject(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
    
    @PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "시스템관리 - 권한 ", description = "권한 목록")
    @GetMapping(value = "/system/auth")
    public ResponseEntity<PageResponse<Map<String, Object>>> systemAuth(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();

		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.loginInfo(loginInfo)
				.build());
    }
}
