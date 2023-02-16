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
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.service.ReqService;
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
	private final ReqService reqService; 
	
    @Operation(summary = "소속 페이지 정보", description = "소속 페이지 정보")
    @GetMapping(value = "/company")
    public ResponseEntity<ListResponse<List<CompInfo>>> pageCompanyList(HttpServletRequest request
    		, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	log.info("loginUserInfo: {}", loginUserInfo);
    	
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, compService.countComp(map)));
    	
    	List<CompInfo> complist = compService.listComp(map);
    	
		return ResponseEntity.ok(ListResponse.<List<CompInfo>>builder()
				.request(request)
				.data(complist)
				.query(map)
				.loginUserInfo(loginUserInfo)
				.build());
    }
	
    @Operation(summary = "요청 사항 페이지 정보", description = "요청 사항 페이지 정보")
    @GetMapping(value = "/request/{reqSeq}")
    public ResponseEntity<Response<ReqInfo>> pageRequestList(HttpServletRequest request
    		, @PathVariable long reqSeq
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
    	
    	log.info("loginUserInfo: {}", loginUserInfo);
    	
		ReqInfo req = reqService.getReqBySeq(reqSeq);
    	
		return ResponseEntity.ok(Response.<ReqInfo>builder()
				.request(request)
				.data(req)
				.loginUserInfo(loginUserInfo)
				.build());
    }
}
