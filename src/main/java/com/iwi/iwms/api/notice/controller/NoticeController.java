package com.iwi.iwms.api.notice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
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
@RequestMapping("/iwms/notice")
public class NoticeController {

	private final NoticeService noticeService;
	
	@Operation(summary = "공지사항 목록", description = "공지사항 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<NoticeInfo>>> listNotice(HttpServletRequest request
			, @RequestParam(value = "compSeq", required = false) Optional<String> compSeq
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate) {
		
		Map<String, Object> map = new HashMap<>();
		if(compSeq.isPresent()) {
			map.put("compSeq", compSeq.get());
		}
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, noticeService.countNotice(map)));
		
		List<NoticeInfo> noticeList = noticeService.listNotice(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<NoticeInfo>>builder()
				.request(request)
				.data(noticeList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "공지사항 상세", description = "공지사항 상세")
    @GetMapping(value = "/{noticeSeq}")
    public ResponseEntity<ApiResponse<NoticeInfo>> getNoticeBySeq(HttpServletRequest request
    		, @PathVariable long noticeSeq) {
    	
    	NoticeInfo notice = noticeService.getNoticeBySeq(noticeSeq);
    	noticeService.updateViewCnt(noticeSeq);
    	
		return ResponseEntity.ok(ApiResponse.<NoticeInfo>builder()
				.request(request)
				.data(notice)
				.build());
    }
    
    @Operation(summary = "공지사항 등록", description = "공지사항 등록")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertNotice(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @ModelAttribute @Valid Notice notice) {
    	
    	noticeService.insertNotice(notice.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "공지사항 수정", description = "공지사항 수정")
    @PutMapping(value = "/{noticeSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateNotice(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long noticeSeq
			, @ModelAttribute @Valid Notice notice) {
    	
    	boolean result = noticeService.updateNotice(notice.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
	@DeleteMapping(value = "/{noticeSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteNotice(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long noticeSeq
			, @Parameter(hidden = true) Notice notice) {
    	
    	boolean result = noticeService.deleteNotice(notice.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
}
