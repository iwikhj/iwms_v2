package com.iwi.iwms.api.menu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.auth.domain.AuthInfo;
import com.iwi.iwms.api.auth.service.AuthService;
import com.iwi.iwms.api.common.response.ListResponse;
import com.iwi.iwms.api.common.response.Response;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.service.CompService;
import com.iwi.iwms.api.comp.service.ProjService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.notice.domain.NoticeInfo;
import com.iwi.iwms.api.notice.service.NoticeService;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.service.ReqDtlService;
import com.iwi.iwms.api.req.service.ReqService;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.service.UserService;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Menu page", description = "IWMS 메뉴별 페이지 정보")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.root}/${app.version}/pages")
public class MenuPageController {
	
	private static final int DEFAULT_PAGE = 1;
	
	private static final int DEFAULT_LIMIT = 15;
	
	private final NoticeService noticeService;
	
	private final ReqService reqService;
	
	private final ReqDtlService reqDtlService;
	
	private final ProjService projService;
	
	private final UserService userService;
	
	private final CompService compService;
	
	private final AuthService authService;
    
	/**
	 * 메뉴 홈
	 */
    @Operation(summary = "홈 (No data)", description = "홈")
    @GetMapping(value = "/home")
    public ResponseEntity<Response<Void>> home(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		// TODO 홈 비지니스 로직 없음
    	
		return ResponseEntity.ok(Response.<Void>builder()
				.request(request)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
	/**
	 * 메뉴 공지사항
	 */
    @Operation(summary = "공지사항", description = "공지사항 목록")
    @GetMapping(value = "/notice")
    public ResponseEntity<ListResponse<List<NoticeInfo>>> notice(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("pagination", new Pagination(DEFAULT_PAGE, DEFAULT_LIMIT, noticeService.countNotice(map)));
    	List<NoticeInfo> listNotice = noticeService.listNotice(map);
    	
		return ResponseEntity.ok(ListResponse.<List<NoticeInfo>>builder()
				.request(request)
				.data(listNotice)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "공지사항 - 상세", description = "공지사항 상세")
    @GetMapping(value = "/notice/detail")
    public ResponseEntity<Response<NoticeInfo>> noticeDetail(HttpServletRequest request
    		, @RequestParam(value = "seq", required = true) Long noticeSeq
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	NoticeInfo notice = noticeService.getNoticeBySeq(noticeSeq, loginUserInfo.getUserSeq());
    	
		return ResponseEntity.ok(Response.<NoticeInfo>builder()
				.request(request)
				.data(notice)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
	/**
	 * 메뉴 유지보수
	 */
    @Operation(summary = "유지보수 - 유지보수 ", description = "유지보수 목록")
    @GetMapping(value = "/maintain/request")
    public ResponseEntity<ListResponse<List<ReqInfo>>> maintainRequest(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("pagination", new Pagination(DEFAULT_PAGE, DEFAULT_LIMIT, reqService.countReq(map)));
    	
    	List<ReqInfo> listReq = reqService.listReq(map);
    	
		return ResponseEntity.ok(ListResponse.<List<ReqInfo>>builder()
				.request(request)
				.data(listReq)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "유지보수 - 유지보수 - 상세 ", description = "유지보수 상세")
    @GetMapping(value = "/maintain/request/detail")
    public ResponseEntity<Response<ReqDtlInfo>> maintainRequestDetail(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @RequestParam(value = "rSeq", required = true) Long reqSeq
    		, @RequestParam(value = "dSeq", required = false) Long reqDtlSeq) {
    	
    	Map<String, Object> map = new HashMap<>();
    	map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("reqSeq", reqSeq);
		if(reqDtlSeq != null && reqDtlSeq != 0) {
			map.put("reqDtlSeq", reqDtlSeq);
		}
		
    	ReqDtlInfo reqDtl = reqDtlService.getReqDtlByReqAndDtlSeq(map);
    	
		return ResponseEntity.ok(Response.<ReqDtlInfo>builder()
				.request(request)
				.data(reqDtl)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "유지보수 - 기능개발 (No data)", description = "유지보수 기능개발")
    @GetMapping(value = "/maintain/develop")
    public ResponseEntity<Response<Void>> maintainDevelop(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	// TODO 유지보수 기능개발 비지니스 로직 없음
    	
		return ResponseEntity.ok(Response.<Void>builder()
				.request(request)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "유지보수 - 일정관리 (No data)", description = "유지보수 일정관리")
    @GetMapping(value = "/maintain/schedule")
    public ResponseEntity<Response<Void>> maintainSchedule(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	// TODO 유지보수 일정관리 비지니스 로직 없음
    	
		return ResponseEntity.ok(Response.<Void>builder()
				.request(request)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "유지보수 - 현황 (No data)", description = "유지보수 현황")
    @GetMapping(value = "/maintain/statistics")
    public ResponseEntity<Response<Void>> maintainDashboard(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		// TODO 유지보수 현황 비지니스 로직 없음
    	
		return ResponseEntity.ok(Response.<Void>builder()
				.request(request)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
	/**
	 * 메뉴 프로젝트
	 */    
    @Operation(summary = "프로젝트", description = "프로젝트 목록")
    @GetMapping(value = "/project")
    public ResponseEntity<ListResponse<List<ProjInfo>>> project(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("pagination", new Pagination(DEFAULT_PAGE, DEFAULT_LIMIT, projService.countProj(map)));
		
		List<ProjInfo> listProj = projService.listProj(map);
    	
		return ResponseEntity.ok(ListResponse.<List<ProjInfo>>builder()
				.request(request)
				.data(listProj)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
	/**
	 * 메뉴 이력관리
	 */
    @Operation(summary = "이력관리 (No data)", description = "이력관리")
    @GetMapping(value = "/history")
    public ResponseEntity<Response<Void>> history(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	// TODO 이력관리 비지니스 로직 없음
    	
		return ResponseEntity.ok(Response.<Void>builder()
				.request(request)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    
	/**
	 * 메뉴 배포관리
	 */
    @Operation(summary = "배포관리 (No data)", description = "배포관리")
    @GetMapping(value = "/deploy")
    public ResponseEntity<Response<Void>> deploy(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	// TODO 배포관리 비지니스 로직 없음
    	
		return ResponseEntity.ok(Response.<Void>builder()
				.request(request)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    
	/**
	 * 메뉴 시스템관리
	 */
    @PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "시스템관리 - 사용자", description = "사용자 목록")
    @GetMapping(value = "/system/user")
    public ResponseEntity<ListResponse<List<UserInfo>>> systemUser(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("pagination", new Pagination(DEFAULT_PAGE, DEFAULT_LIMIT, userService.countUser(map)));
    	
    	List<UserInfo> listUser = userService.listUser(map);
    	
		return ResponseEntity.ok(ListResponse.<List<UserInfo>>builder()
				.request(request)
				.data(listUser)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "시스템관리 - 소속 ", description = "소속 목록")
    @GetMapping(value = "/system/company")
    public ResponseEntity<ListResponse<List<CompInfo>>> systemCompany(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("pagination", new Pagination(DEFAULT_PAGE, DEFAULT_LIMIT, compService.countComp(map)));
    	
		List<CompInfo> listComp = compService.listComp(map);
    	
		return ResponseEntity.ok(ListResponse.<List<CompInfo>>builder()
				.request(request)
				.data(listComp)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "시스템관리 - 프로젝트 ", description = "프로젝트 목록")
    @GetMapping(value = "/system/project")
    public ResponseEntity<ListResponse<List<ProjInfo>>> systemProject(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("pagination", new Pagination(DEFAULT_PAGE, DEFAULT_LIMIT, projService.countProj(map)));
		
		List<ProjInfo> listProj = projService.listProj(map);
    	
		return ResponseEntity.ok(ListResponse.<List<ProjInfo>>builder()
				.request(request)
				.data(listProj)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
    @Operation(summary = "시스템관리 - 권한 ", description = "권한 목록")
    @GetMapping(value = "/system/auth")
    public ResponseEntity<ListResponse<List<AuthInfo>>> systemAuth(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("pagination", new Pagination(DEFAULT_PAGE, DEFAULT_LIMIT, authService.countAuth(map)));
		
		List<AuthInfo> listAuth = authService.listAuth(map);

		return ResponseEntity.ok(ListResponse.<List<AuthInfo>>builder()
				.request(request)
				.data(listAuth)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
}
