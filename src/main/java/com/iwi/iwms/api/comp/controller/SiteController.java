package com.iwi.iwms.api.comp.controller;

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

import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.comp.domain.Site;
import com.iwi.iwms.api.comp.domain.SiteInfo;
import com.iwi.iwms.api.comp.service.SiteService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Site", description = "IWMS 사이트 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/site")
public class SiteController {
	
	private final SiteService siteService; 

	@Operation(summary = "사이트 목록", description = "사이트 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<SiteInfo>>> listSite(HttpServletRequest request
			, @RequestParam(value = "compSeq", required = false) Optional<String> compSeq
			, @RequestParam(value = "projSeq", required = false) Optional<String> projSeq
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate) {
		
		Map<String, Object> map = new HashMap<>();
		if(compSeq.isPresent()) {
			map.put("compSeq", compSeq.get());
		}
		if(projSeq.isPresent()) {
			map.put("projSeq", projSeq.get());
		}
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, siteService.countSite(map)));
		
		List<SiteInfo> SiteList = siteService.listSite(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<SiteInfo>>builder()
				.request(request)
				.data(SiteList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "사이트 정보", description = "사이트 정보")
    @GetMapping(value = "/{siteSeq}")
    public ResponseEntity<ApiResponse<SiteInfo>> getSiteBySeq(HttpServletRequest request
    		, @PathVariable long siteSeq
			, @Parameter(hidden = true) Site site) {
    	
    	SiteInfo Siteect = siteService.getSiteBySeq(site);
    	
		return ResponseEntity.ok(ApiResponse.<SiteInfo>builder()
				.request(request)
				.data(Siteect)
				.build());
    }
    
    @Operation(summary = "사이트 등록", description = "사이트 등록")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertSite(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @ModelAttribute @Valid Site site) {
    	
    	siteService.insertSite(site.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "사이트 수정", description = "사이트 수정")
	@PutMapping(value = "/{siteSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateSite(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long siteSeq
			, @ModelAttribute @Valid Site site) {
    	
    	boolean result = siteService.updateSite(site.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "사이트 삭제", description = "사이트 삭제")
	@DeleteMapping(value = "/{siteSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteSite(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long siteSeq
			, @Parameter(hidden = true) Site site) {
    	
    	boolean result = siteService.deleteSite(site.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
}
