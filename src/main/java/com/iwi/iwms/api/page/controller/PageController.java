package com.iwi.iwms.api.page.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.response.ListResponse;
import com.iwi.iwms.api.common.response.Response;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.service.CompService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.notice.domain.NoticeInfo;
import com.iwi.iwms.api.notice.service.NoticeService;
import com.iwi.iwms.api.req.domain.ReqInfo;
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
@Tag(name = "Page", description = "IWMS 페이지 정보")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/page")
public class PageController {
	
	private final CompService compService;
	
	private final UserService userService;
	
	private final ReqService reqService;
	
	private final NoticeService noticeService;
	
    @Operation(summary = "소속 목록 페이지 정보", description = "소속 목록 페이지 정보")
    @GetMapping(value = "/company")
    public ResponseEntity<ListResponse<List<CompInfo>>> pageCompany(HttpServletRequest request
    		, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, compService.countComp(map)));
    	
    	List<CompInfo> compList = compService.listComp(map);
    	
		return ResponseEntity.ok(ListResponse.<List<CompInfo>>builder()
				.request(request)
				.data(compList)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "소속 상세 페이지 정보", description = "소속 상세 페이지 정보")
    @GetMapping(value = "/company/{compSeq}")
    public ResponseEntity<Response<CompInfo>> pageCompanyDetail(HttpServletRequest request
    		, @PathVariable long compSeq
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	CompInfo comp = compService.getCompBySeq(compSeq);
    	
		return ResponseEntity.ok(Response.<CompInfo>builder()
				.request(request)
				.data(comp)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "사용자 목록 페이지 정보", description = "사용자 목록 페이지 정보")
    @GetMapping(value = "/user")
    public ResponseEntity<ListResponse<List<UserInfo>>> pageUser(HttpServletRequest request
    		, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, userService.countUser(map)));
    	
    	List<UserInfo> userList = userService.listUser(map);
    	
		return ResponseEntity.ok(ListResponse.<List<UserInfo>>builder()
				.request(request)
				.data(userList)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "사용자 상세 페이지 정보", description = "사용자 상세 페이지 정보")
    @GetMapping(value = "/user/{userSeq}")
    public ResponseEntity<Response<UserInfo>> pageUserDetail(HttpServletRequest request
    		, @PathVariable long userSeq
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	UserInfo user = userService.getUserBySeq(userSeq);
    	
		return ResponseEntity.ok(Response.<UserInfo>builder()
				.request(request)
				.data(user)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    
    @Operation(summary = "요청사항 목록 페이지 정보", description = "요청사항 목록 페이지 정보")
    @GetMapping(value = "/request")
    public ResponseEntity<ListResponse<List<ReqInfo>>> pageReq(HttpServletRequest request
    		, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, reqService.countReq(map)));
    	
    	List<ReqInfo> reqList = reqService.listReq(map);
    	
		return ResponseEntity.ok(ListResponse.<List<ReqInfo>>builder()
				.request(request)
				.data(reqList)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "요청사항 상세 페이지 정보", description = "요청사항 상세 페이지 정보")
    @GetMapping(value = "/request/{reqSeq}")
    public ResponseEntity<Response<ReqInfo>> pageReqDetail(HttpServletRequest request
    		, @PathVariable long reqSeq
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	ReqInfo req = reqService.getReqBySeq(reqSeq);
    	
		return ResponseEntity.ok(Response.<ReqInfo>builder()
				.request(request)
				.data(req)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "공지사항 목록 페이지 정보", description = "공지사항 목록 페이지 정보")
    @GetMapping(value = "/notice")
    public ResponseEntity<ListResponse<List<NoticeInfo>>> pageNotice(HttpServletRequest request
    		, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, noticeService.countNotice(map)));
    	
    	List<NoticeInfo> noticeList = noticeService.listNotice(map);
    	
		return ResponseEntity.ok(ListResponse.<List<NoticeInfo>>builder()
				.request(request)
				.data(noticeList)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
    
    @Operation(summary = "공지사항 상세 페이지 정보", description = "공지사항 상세 페이지 정보")
    @GetMapping(value = "/notice/{noticeSeq}")
    public ResponseEntity<Response<NoticeInfo>> pageNoticeDetail(HttpServletRequest request
    		, @PathVariable long noticeSeq
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	NoticeInfo notice = noticeService.getNoticeBySeq(noticeSeq);
    	
		return ResponseEntity.ok(Response.<NoticeInfo>builder()
				.request(request)
				.data(notice)
				.loginUserInfo(loginUserInfo)
				.build());
    }
}
