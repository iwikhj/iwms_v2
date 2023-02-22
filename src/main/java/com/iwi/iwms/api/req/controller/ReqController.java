package com.iwi.iwms.api.req.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.service.ReqService;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Request", description = "IWMS 요청 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/request")
public class ReqController {

	private final ReqService reqService;
	
	@Operation(summary = "요청 목록", description = "요청 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<ReqInfo>>> listReq(HttpServletRequest request
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, reqService.countReq(map)));
		
		List<ReqInfo> reqList = reqService.listReq(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<ReqInfo>>builder()
				.request(request)
				.data(reqList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "요청 정보", description = "요청 정보")
    @GetMapping(value = "/{reqSeq}")
    public ResponseEntity<ApiResponse<ReqInfo>> getReqBySeq(HttpServletRequest request
			, @Parameter(hidden = true) Req req) {
    	
    	ReqInfo requ = reqService.getReqBySeq(req);
    	
		return ResponseEntity.ok(ApiResponse.<ReqInfo>builder()
				.request(request)
				.data(requ)
				.build());
    }
    
    @Operation(summary = "요청 등록", description = "요청 등록")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @ModelAttribute @Valid Req req) {
    	
    	reqService.insertReq(req.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "요청 수정", description = "요청 수정")
	@PutMapping(value = "/{reqSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @ModelAttribute @Valid Req req) {
    	
    	boolean result = reqService.updateReq(req.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청 삭제", description = "요청 삭제")
	@DeleteMapping(value = "/{reqSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @Parameter(hidden = true) Req req) {
    	
    	boolean result = reqService.deleteReq(req.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
}
