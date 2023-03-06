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
import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqAgree;
import com.iwi.iwms.api.req.domain.ReqCancel;
import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlCmt;
import com.iwi.iwms.api.req.domain.ReqDtlUser;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.service.ReqDtlCmtService;
import com.iwi.iwms.api.req.service.ReqDtlService;
import com.iwi.iwms.api.req.service.ReqDtlUserService;
import com.iwi.iwms.api.req.service.ReqService;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Request", description = "IWMS 유지보수 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/request")
public class ReqController {

	private final ReqService reqService;
	
	private final ReqDtlService reqDtlService;
	
	private final ReqDtlCmtService reqDtlCmtService;
	
	private final ReqDtlUserService reqDtlUserCmtService;
	
	@Operation(summary = "요청사항 목록", description = "요청사항 목록")
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
	
    @Operation(summary = "요청사항 상세", description = "요청사항 상세")
    @GetMapping(value = "/{reqSeq}")
    public ResponseEntity<ApiResponse<ReqInfo>> getReqBySeq(HttpServletRequest request
    		, @PathVariable long reqSeq) {
    	
    	ReqInfo requ = reqService.getReqBySeq(reqSeq);
    	
		return ResponseEntity.ok(ApiResponse.<ReqInfo>builder()
				.request(request)
				.data(requ)
				.build());
    }
    
    @Operation(summary = "요청사항 등록", description = "요청사항 등록")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @ModelAttribute @Valid Req req) {
    	
    	reqService.insertReq(req.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "요청사항 수정", description = "요청사항 수정")
	@PutMapping(value = "/{reqSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    
    @Operation(summary = "요청사항 삭제", description = "요청사항 삭제")
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
    
    @Operation(summary = "요청사항 취소", description = "요청사항 취소")
	@PatchMapping(value = "/{reqSeq}/cancel")
	public ResponseEntity<ApiResponse<Boolean>> cancelReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @Parameter(hidden = true) ReqCancel cancel) {
    	
    	boolean result = reqService.cancelReq(cancel.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 합의", description = "요청사항 합의")
	@PatchMapping(value = "/{reqSeq}/agree", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> agreeReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @ModelAttribute @Valid ReqAgree agree) {

    	boolean result = reqService.agreeReq(agree.of(loginUserInfo)) > 0 ? true : false;
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 상세 등록", description = "요청사항 상세 등록")
	@PostMapping(value = "/{reqSeq}/detail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertReqDtl(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @ModelAttribute @Valid ReqDtl reqDtl) {
    	
    	reqDtlService.insertReqDtl(reqDtl.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "요청사항 상세 수정", description = "요청사항 상세 수정")
	@PutMapping(value = "/{reqSeq}/detail/{reqDtlSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateReqDtl(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @ModelAttribute @Valid ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.updateReqDtl(reqDtl.of(loginUserInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 상세 삭제", description = "요청사항 상세 삭제")
	@DeleteMapping(value = "/{reqSeq}/detail/{reqDtlSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReqDtl(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Parameter(hidden = true) ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.deleteReqDtl(reqDtl.of(loginUserInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 상세 코멘트 등록", description = "요청사항 상세 코멘트 등록")
	@PostMapping(value = "/{reqSeq}/detail/{reqDtlSeq}/comment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertReqDtlCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @ModelAttribute @Valid ReqDtlCmt reqDtlCmt) {
    	
    	reqDtlCmtService.insertReqDtlCmt(reqDtlCmt.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "요청사항 상세 코멘트 수정", description = "요청사항 상세 코멘트 수정")
	@PutMapping(value = "/{reqSeq}/detail/{reqDtlSeq}/comment/{reqDtlCmtSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateReqDtlCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @PathVariable long reqDtlCmtSeq
			, @ModelAttribute @Valid ReqDtlCmt reqDtlCmt) {
    	
    	boolean result = reqDtlCmtService.updateReqDtlCmt(reqDtlCmt.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 상세 코멘트 삭제", description = "요청사항 상세 코멘트 삭제")
	@DeleteMapping(value = "/{reqSeq}/detail/{reqDtlSeq}/comment/{reqDtlCmtSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReqDtlCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @PathVariable long reqDtlCmtSeq
			, @Parameter(hidden = true) ReqDtlCmt reqDtlCmt) {
    	
    	boolean result = reqDtlCmtService.deleteReqDtlCmt(reqDtlCmt.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    
    @Operation(summary = "요청사항 상세 담당자 처리 등록", description = "요청사항 상세 담당자 처리 등록")
	@PostMapping(value = "/{reqSeq}/detail/{reqDtlSeq}/process", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertReqDtlUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @ModelAttribute @Valid ReqDtlUser reqDtlUser) {
    	
    	reqDtlUserCmtService.insertReqDtlUser(reqDtlUser.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    
    @Operation(summary = "요청사항 상세 담당자 처리 수정", description = "요청사항 상세 담당자 처리 수정")
	@PutMapping(value = "/{reqSeq}/detail/{reqDtlSeq}/process/{reqDtlUserSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateReqDtlUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @PathVariable long reqDtlUserSeq
			, @ModelAttribute @Valid ReqDtlUser reqDtlUser) {
    	
    	boolean result = reqDtlUserCmtService.updateReqDtlUser(reqDtlUser.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 상세 담당자 처리 삭제", description = "요청사항 상세 담당자 처리 삭제")
	@DeleteMapping(value = "/{reqSeq}/detail/{reqDtlSeq}/process/{reqDtlUserSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReqDtlUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @PathVariable long reqDtlUserSeq
			, @Parameter(hidden = true) ReqDtlUser reqDtlUser) {
    	
    	boolean result = reqDtlUserCmtService.deleteReqDtlUser(reqDtlUser.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
}
