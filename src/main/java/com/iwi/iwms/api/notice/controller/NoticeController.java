package com.iwi.iwms.api.notice.controller;

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
import com.iwi.iwms.api.notice.domain.Notice;
import com.iwi.iwms.api.notice.domain.NoticeInfo;
import com.iwi.iwms.api.notice.service.NoticeService;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Notice", description = "IWMS 공지사항 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.path}/notices")
public class NoticeController {

	private final NoticeService noticeService;
	
	@Operation(summary = "공지사항 목록", description = "공지사항 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<NoticeInfo>>> listNotice(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "size", required = false, defaultValue = "10") int size
			, @RequestParam(value = "keykind", required = false) String keykind
			, @RequestParam(value = "keyword", required = false) String keyword) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("keykind", keykind); 
		map.put("keyword", keyword); 
		map.put("loginUserSeq", loginInfo.getUserSeq()); 
		map.put("pagination", new Pagination(page, size, noticeService.countNotice(map)));
		
		List<NoticeInfo> noticeList = noticeService.listNotice(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<NoticeInfo>>builder()
				.data(noticeList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "공지사항 상세 정보", description = "공지사항 상세 정보")
    @GetMapping(value = "/{noticeSeq}")
    public ResponseEntity<ApiResponse<NoticeInfo>> getNoticeBySeq(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo
    		, @PathVariable long noticeSeq) {
    	
    	NoticeInfo noticeInfo = noticeService.getNoticeBySeq(noticeSeq, loginInfo.getUserSeq());
    	
		return ResponseEntity.ok(ApiResponse.<NoticeInfo>builder()
				.data(noticeInfo)
				.build());
    }
    
    @Operation(summary = "공지사항 등록", description = "공지사항 등록")
	@PostMapping(value = "")
	public ResponseEntity<ApiResponse<Boolean>> insertNotice(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @Valid Notice notice) {
    	
    	noticeService.insertNotice(notice.of(loginInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "공지사항 수정", description = "공지사항 수정")
    @PutMapping(value = "/{noticeSeq}")
	public ResponseEntity<ApiResponse<Boolean>> updateNotice(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long noticeSeq
			, @Valid Notice notice) {
    	
    	boolean result = noticeService.updateNotice(notice.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
	@DeleteMapping(value = "/{noticeSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteNotice(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long noticeSeq
			, @Parameter(hidden = true) Notice notice) {
    	
    	boolean result = noticeService.deleteNotice(notice.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "공지사항 조회수 증가", description = "공지사항 조회수 증가")
	@PatchMapping(value = "/{noticeSeq}")
	public ResponseEntity<ApiResponse<Boolean>> updateNoticeViewCnt(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo
    		, @PathVariable long noticeSeq
    		, @Parameter(hidden = true) Notice notice) {
    	
    	boolean result = noticeService.updateViewCnt(notice.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
}
