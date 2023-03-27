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
import com.iwi.iwms.api.req.domain.Cmt;
import com.iwi.iwms.api.req.domain.CmtInfo;
import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;
import com.iwi.iwms.api.req.domain.ReqHis;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.service.ReqCmtService;
import com.iwi.iwms.api.req.service.ReqDtlCmtService;
import com.iwi.iwms.api.req.service.ReqDtlService;
import com.iwi.iwms.api.req.service.ReqService;
import com.iwi.iwms.utils.Pagination;
import com.iwi.iwms.utils.PredicateMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Request", description = "IWMS 유지보수 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.root}/${app.version}/requests")
public class ReqController {

	private final ReqService reqService;
	
	private final ReqCmtService reqCmtService;
	
	private final ReqDtlService reqDtlService;
	
	private final ReqDtlCmtService reqDtlCmtService;

	@Operation(summary = "요청사항 목록", description = "요청사항 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<ReqInfo>>> listReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "busiRollCd", required = false) String busiRollCd
			, @RequestParam(value = "reqStatCd", required = false) String reqStatCd
			, @RequestParam(value = "siteSeq", required = false) Long siteSeq
			, @RequestParam(value = "reqStdYmd", required = false) String reqStdYmd
			, @RequestParam(value = "reqEndYmd", required = false) String reqEndYmd
			, @RequestParam(value = "reqTitle", required = false) String reqTitle
			, @RequestParam(value = "regNm", required = false) String regNm
			, @RequestParam(value = "reqDtlUser", required = false) String reqDtlUser) {
		
		Map<String, Object> map = PredicateMap.make(request, loginUserInfo);
		map.put("pagination", new Pagination(page, limit, reqService.countReq(map)));
		List<ReqInfo> reqList = reqService.listReq(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<ReqInfo>>builder()
				.data(reqList)
				.query(map)
				.build());
	}
    
    @Operation(summary = "요청사항 등록", description = "요청사항 등록")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @ModelAttribute @Valid Req req) {
    	
    	reqService.insertReq(req.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
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
				.data(result)
				.build());
	}

    @Operation(summary = "요청사항 상태 변경 이력 등록", description = "합의/협의요청/반려/취소")
	@PostMapping(value = "/{reqSeq}/status", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> agreeReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @ModelAttribute @Valid ReqHis reqHis) {

    	reqService.insertReqHis(reqHis.of(loginUserInfo));
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}

    @Operation(summary = "요청사항 코멘트 등록", description = "요청사항 코멘트 등록")
	@PostMapping(value = "/{reqSeq}/comments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<CmtInfo>> insertReqCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @ModelAttribute @Valid Cmt cmt) {
    	
    	CmtInfo cmtInfo = reqCmtService.insertReqCmt(cmt.of(loginUserInfo));

    	return ResponseEntity.ok(ApiResponse.<CmtInfo>builder()
				.data(cmtInfo)
				.build());
	}
    
    @Operation(summary = "요청사항 코멘트 수정", description = "요청사항 코멘트 수정")
	@PutMapping(value = "/{reqSeq}/comments/{cmtSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<CmtInfo>> updateReqCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long cmtSeq
			, @ModelAttribute @Valid Cmt cmt) {
    	
    	CmtInfo cmtInfo = reqCmtService.updateReqCmt(cmt.of(loginUserInfo));

    	return ResponseEntity.ok(ApiResponse.<CmtInfo>builder()
				.data(cmtInfo)
				.build());
	}
    
    @Operation(summary = "요청사항 코멘트 삭제", description = "요청사항 코멘트 삭제")
	@DeleteMapping(value = "/{reqSeq}/comments/{cmtSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReqCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long cmtSeq
			, @Parameter(hidden = true) Cmt cmt) {
    	
    	boolean result = reqCmtService.deleteReqCmt(cmt.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}    
    
    @Operation(summary = "요청사항 상세 정보", description = "상세 정보")
    @GetMapping(value = "/{reqSeq}/details")
    public ResponseEntity<ApiResponse<ReqDtlInfo>> getReqDtlByReqAndDtlSeq(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
    		, @PathVariable long reqSeq
			, @RequestParam(value = "reqDtlSeq", required = false) Long reqDtlSeq) {
		
    	Map<String, Object> map = new HashMap<>();
    	map.put("loginUserSeq", loginUserInfo.getUserSeq());
		map.put("reqSeq", reqSeq);
		if(reqDtlSeq != null && reqDtlSeq != 0) {
			map.put("reqDtlSeq", reqDtlSeq);
		}
		
    	ReqDtlInfo reqDtlInfo = reqDtlService.getReqDtlByReqAndDtlSeq(map);
    	
		return ResponseEntity.ok(ApiResponse.<ReqDtlInfo>builder()
				.data(reqDtlInfo)
				.build());
    }

    @Operation(summary = "요청사항 상세 작업 담당자 배정", description = "상세 작업 담당자 배정")
	@PostMapping(value = "/{reqSeq}/details", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertReqDtl(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @ModelAttribute @Valid ReqDtl reqDtl) {
    	
    	reqDtlService.insertReqDtl(reqDtl.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "상세 작업 담당자 수정", description = "상세 작업 담당자 수정")
	@PutMapping(value = "/{reqSeq}/details/{reqDtlSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateReqDtl(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @ModelAttribute @Valid ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.updateReqDtl(reqDtl.of(loginUserInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "상세 작업 담당자 삭제", description = "상세 작업 담당자 삭제")
	@DeleteMapping(value = "/{reqSeq}/details/{reqDtlSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReqDtl(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Parameter(hidden = true) ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.deleteReqDtl(reqDtl.of(loginUserInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 담당자별 상세 상태 변경 - 담당자 확인", description = "담당자 확인 상태 업데이트")
	@PatchMapping(value = "/{reqSeq}/details/{reqDtlSeq}/inprogress")
	public ResponseEntity<ApiResponse<Boolean>> updateReqDtlStatByInProgress(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Parameter(hidden = true) ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.updateReqDtlStatByInProgress(reqDtl.of(loginUserInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 담당자별 상세 상태 변경 - 처리 완료", description = "처리 완료 상태 업데이트")
    @PatchMapping(value = "/{reqSeq}/details/{reqDtlSeq}/processed", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateReqDtlStatByProcessed(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @ModelAttribute @Valid ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.updateReqDtlStatByProcessed(reqDtl.of(loginUserInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 담당자별 상세 상태 변경 - 검수 완료", description = "검수 완료 상태 업데이트")
    @PatchMapping(value = "/{reqSeq}/details/{reqDtlSeq}/completed", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateReqDtlStatByInspectionCompleted(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @ModelAttribute @Valid ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.updateReqDtlStatByInspectionCompleted(reqDtl.of(loginUserInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 담당자별 상세 상태 변경 - 취소", description = "취소 상태 업데이트")
    @PatchMapping(value = "/{reqSeq}/details/{reqDtlSeq}/cancel")
	public ResponseEntity<ApiResponse<Boolean>> updateReqDtlStatByCancel(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Parameter(hidden = true) ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.updateReqDtlStatByCancel(reqDtl.of(loginUserInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 담당자별 상세 코멘트 등록", description = "요청사항 상세 코멘트 등록")
	@PostMapping(value = "/{reqSeq}/details/{reqDtlSeq}/comments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<CmtInfo>> insertReqDtlCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @ModelAttribute @Valid Cmt cmt) {
    	
    	CmtInfo cmtInfo = reqDtlCmtService.insertReqDtlCmt(cmt.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<CmtInfo>builder()
				.data(cmtInfo)
				.build());
	}
    
    @Operation(summary = "요청사항 담당자별 상세 코멘트 수정", description = "요청사항 상세 코멘트 수정")
	@PutMapping(value = "/{reqSeq}/details/{reqDtlSeq}/comments/{cmtSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<CmtInfo>> updateReqDtlCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @PathVariable long cmtSeq
			, @ModelAttribute @Valid Cmt cmt) {
    	
    	CmtInfo cmtInfo = reqDtlCmtService.updateReqDtlCmt(cmt.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<CmtInfo>builder()
				.data(cmtInfo)
				.build());
	}
    
    @Operation(summary = "요청사항 담당자별 상세 코멘트 삭제", description = "요청사항 상세 코멘트 삭제")
	@DeleteMapping(value = "/{reqSeq}/details/{reqDtlSeq}/comments/{cmtSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReqDtlCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @PathVariable long cmtSeq
			, @Parameter(hidden = true) Cmt cmt) {
    	
    	boolean result = reqDtlCmtService.deleteReqDtlCmt(cmt.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
}
