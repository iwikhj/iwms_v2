package com.iwi.iwms.api.req.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.login.domain.LoginInfo;
import com.iwi.iwms.api.req.domain.Cmt;
import com.iwi.iwms.api.req.domain.CmtInfo;
import com.iwi.iwms.api.req.domain.His;
import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.enums.ReqDtlStatCode;
import com.iwi.iwms.api.req.enums.ReqStatCode;
import com.iwi.iwms.api.req.service.ReqCmtService;
import com.iwi.iwms.api.req.service.ReqDtlCmtService;
import com.iwi.iwms.api.req.service.ReqDtlService;
import com.iwi.iwms.api.req.service.ReqService;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Request", description = "IWMS 유지보수 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.path}/requests")
public class ReqController {

	private final ReqService reqService;
	
	private final ReqCmtService reqCmtService;
	
	private final ReqDtlService reqDtlService;
	
	private final ReqDtlCmtService reqDtlCmtService;

	@Operation(summary = "요청사항 목록", description = "요청사항 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<ReqInfo>>> listReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "size", required = false, defaultValue = "10") int size
			, @RequestParam(value = "keykind", required = false) String keykind
			, @RequestParam(value = "keyword", required = false) String keyword) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("keykind", keykind); 
		map.put("keyword", keyword); 
		map.put("loginUserSeq", loginInfo.getUserSeq()); 
		map.put("pagination", new Pagination(page, size, reqService.countReq(map)));
		List<ReqInfo> reqList = reqService.listReq(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<ReqInfo>>builder()
				.data(reqList)
				.query(map)
				.build());
	}
 
    
    @Operation(summary = "요청사항 상세 정보", description = "요청사항 상세 정보")
    @GetMapping(value = "/{reqSeq}/detail")
    public ResponseEntity<ApiResponse<ReqDtlInfo>> getReqDtlByReqAndDtlSeq(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo		
    		, @PathVariable long reqSeq
			, @RequestParam(value = "reqDtlSeq", required = false) Long reqDtlSeq) {
		
    	ReqDtlInfo reqDtlInfo = null;
    	
		if(reqDtlSeq == null) {
			reqDtlInfo = reqDtlService.getReqDtlByReqSeq(reqSeq, loginInfo.getUserSeq());
		} else {
			reqDtlInfo = reqDtlService.getReqDtlBySeq(reqSeq, reqDtlSeq, loginInfo.getUserSeq());
		}
    	
		return ResponseEntity.ok(ApiResponse.<ReqDtlInfo>builder()
				.data(reqDtlInfo)
				.build());
    }
    @Operation(summary = "요청사항 등록", description = "요청사항 등록")
	@PostMapping(value = "")
	public ResponseEntity<ApiResponse<Boolean>> insertReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @Valid Req req) {
    	
    	reqService.insertReq(req.of(loginInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "요청사항 수정", description = "요청사항 수정")
	@PutMapping(value = "/{reqSeq}")
	public ResponseEntity<ApiResponse<Boolean>> updateReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @Valid Req req) {
    	
    	boolean result = reqService.updateReq(req.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "요청사항 삭제", description = "요청사항 삭제")
	@DeleteMapping(value = "/{reqSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReq(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @Parameter(hidden = true) Req req) {
    	
    	boolean result = reqService.deleteReq(req.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}

    @Operation(summary = "요청사항 합의", description = "요청사항 합의")
	@PatchMapping(value = "/{reqSeq}/status/agree")
	public ResponseEntity<ApiResponse<Boolean>> updateAgree(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @Valid His his) {

    	ReqStatCode status = ReqStatCode.AGREE;	//AGREE
    	his.setStatCd(status.getCode());
    	
    	reqService.updateReqStat(his.of(loginInfo));
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "요청사항 협의 요청", description = "요청사항 협의 요청")
	@PatchMapping(value = "/{reqSeq}/status/nego")
	public ResponseEntity<ApiResponse<Boolean>> updateNego(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @Valid His his) {

    	ReqStatCode status = ReqStatCode.NEGO_CHANGE;	//NEGO_CHANGE
    	if(his.getNegoGb() == 2) {
    		status = ReqStatCode.NEGO_ADD;				//NEGO_ADD
    	}
    	
    	his.setStatCd(status.getCode());
    	
    	reqService.updateReqStat(his.of(loginInfo));
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "요청사항 반려", description = "요청사항 반려")
	@PatchMapping(value = "/{reqSeq}/status/reject")
	public ResponseEntity<ApiResponse<Boolean>> updateReject(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @Valid His his) {

    	ReqStatCode status = ReqStatCode.REJECT;	//REJECT
    	his.setStatCd(status.getCode());
    	
    	reqService.updateReqStat(his.of(loginInfo));
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "요청사항 취소", description = "요청사항 취소")
	@PatchMapping(value = "/{reqSeq}/status/cancel")
	public ResponseEntity<ApiResponse<Boolean>> updateCancel(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @Valid His his) {

    	ReqStatCode status = ReqStatCode.CANCEL;	//CANCEL
    	his.setStatCd(status.getCode());
    	
    	reqService.updateReqStat(his.of(loginInfo));
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}

    @Operation(summary = "요청사항 코멘트 등록", description = "요청사항 코멘트 등록")
	@PostMapping(value = "/{reqSeq}/comments")
	public ResponseEntity<ApiResponse<CmtInfo>> insertReqCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @Valid Cmt cmt) {
    	
    	CmtInfo cmtInfo = reqCmtService.insertReqCmt(cmt.of(loginInfo));

    	return ResponseEntity.ok(ApiResponse.<CmtInfo>builder()
				.data(cmtInfo)
				.build());
	}
    
    @Operation(summary = "요청사항 코멘트 수정", description = "요청사항 코멘트 수정")
	@PutMapping(value = "/{reqSeq}/comments/{cmtSeq}")
	public ResponseEntity<ApiResponse<CmtInfo>> updateReqCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long cmtSeq
			, @Valid Cmt cmt) {
    	
    	CmtInfo cmtInfo = reqCmtService.updateReqCmt(cmt.of(loginInfo));

    	return ResponseEntity.ok(ApiResponse.<CmtInfo>builder()
				.data(cmtInfo)
				.build());
	}
    
    @Operation(summary = "요청사항 코멘트 삭제", description = "요청사항 코멘트 삭제")
	@DeleteMapping(value = "/{reqSeq}/comments/{cmtSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReqCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long cmtSeq
			, @Parameter(hidden = true) Cmt cmt) {
    	
    	boolean result = reqCmtService.deleteReqCmt(cmt.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}    

    @Operation(summary = "작업 담당자 배정", description = "작업 담당자 배정")
	@PostMapping(value = "/{reqSeq}/tasks")
	public ResponseEntity<ApiResponse<Boolean>> insertReqDtl(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @Valid ReqDtl reqDtl) {
    	
    	reqDtlService.insertReqDtl(reqDtl.of(loginInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "작업 담당자 수정", description = "작업 담당자 수정")
	@PutMapping(value = "/{reqSeq}/tasks/{reqDtlSeq}")
	public ResponseEntity<ApiResponse<Boolean>> updateReqDtl(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Valid ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.updateReqDtl(reqDtl.of(loginInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "작업 담당자 삭제", description = "작업 담당자 삭제")
	@DeleteMapping(value = "/{reqSeq}/tasks/{reqDtlSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReqDtl(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Parameter(hidden = true) ReqDtl reqDtl) {
    	
    	boolean result = reqDtlService.deleteReqDtl(reqDtl.of(loginInfo)) > 0 ? true : false;

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "작업 담당자 접수", description = "작업 담당자 접수")
	@PatchMapping(value = "/{reqSeq}/tasks/{reqDtlSeq}/status/inprogress")
	public ResponseEntity<ApiResponse<Boolean>> updateInProgress(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Parameter(hidden = true) ReqDtl reqDtl) {
    	
    	ReqDtlStatCode status = ReqDtlStatCode.IN_PROGRESS;	//IN_PROGRESS
    	reqDtl.setStatCd(status.getCode());
    	
    	reqDtlService.updateReqDtlStat(reqDtl.of(loginInfo));

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "작업 처리 완료", description = "작업 처리 완료")
    @PatchMapping(value = "/{reqSeq}/tasks/{reqDtlSeq}/status/processed")
	public ResponseEntity<ApiResponse<Boolean>> updateProcessed(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Parameter(hidden = true) ReqDtl reqDtl) {
    	
    	ReqDtlStatCode status = ReqDtlStatCode.PROCESSED;	//PROCESSED
    	reqDtl.setStatCd(status.getCode());
    	
    	reqDtlService.updateReqDtlStat(reqDtl.of(loginInfo));

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "작업 검수 완료", description = "작업 검수 완료")
    @PatchMapping(value = "/{reqSeq}/tasks/{reqDtlSeq}/status/completed")
	public ResponseEntity<ApiResponse<Boolean>> updateInspectionCompleted(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Parameter(hidden = true) ReqDtl reqDtl) {
    	
    	ReqDtlStatCode status = ReqDtlStatCode.INSPECTION_COMPLETED;	//INSPECTION_COMPLETED
    	reqDtl.setStatCd(status.getCode());
    	
    	reqDtlService.updateReqDtlStat(reqDtl.of(loginInfo));

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "작업 취소", description = "작업 취소")
    @PatchMapping(value = "/{reqSeq}/tasks/{reqDtlSeq}/status/cancel")
	public ResponseEntity<ApiResponse<Boolean>> updateCancel2(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Parameter(hidden = true) ReqDtl reqDtl) {
    	
    	ReqDtlStatCode status = ReqDtlStatCode.CANCEL;	//CANCEL
    	reqDtl.setStatCd(status.getCode());
    	
    	reqDtlService.updateReqDtlStat(reqDtl.of(loginInfo));

    	return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "작업 코멘트 등록", description = "작업 코멘트 등록")
	@PostMapping(value = "/{reqSeq}/tasks/{reqDtlSeq}/comments")
	public ResponseEntity<ApiResponse<CmtInfo>> insertReqDtlCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @Valid Cmt cmt) {
    	
    	CmtInfo cmtInfo = reqDtlCmtService.insertReqDtlCmt(cmt.of(loginInfo));

		return ResponseEntity.ok(ApiResponse.<CmtInfo>builder()
				.data(cmtInfo)
				.build());
	}
    
    @Operation(summary = "작업 코멘트 수정", description = "작업 코멘트 수정")
	@PutMapping(value = "/{reqSeq}/tasks/{reqDtlSeq}/comments/{cmtSeq}")
	public ResponseEntity<ApiResponse<CmtInfo>> updateReqDtlCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @PathVariable long cmtSeq
			, @Valid Cmt cmt) {
    	
    	CmtInfo cmtInfo = reqDtlCmtService.updateReqDtlCmt(cmt.of(loginInfo));

		return ResponseEntity.ok(ApiResponse.<CmtInfo>builder()
				.data(cmtInfo)
				.build());
	}
    
    @Operation(summary = "작업 코멘트 삭제", description = "작업 코멘트 삭제")
	@DeleteMapping(value = "/{reqSeq}/tasks/{reqDtlSeq}/comments/{cmtSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteReqDtlCmt(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long reqSeq
			, @PathVariable long reqDtlSeq
			, @PathVariable long cmtSeq
			, @Parameter(hidden = true) Cmt cmt) {
    	
    	boolean result = reqDtlCmtService.deleteReqDtlCmt(cmt.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
}
