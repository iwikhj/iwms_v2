package com.iwi.iwms.api.comp.controller;

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

import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.comp.domain.Proj;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.service.ProjService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Company Project", description = "IWMS 프로젝트 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/company/")
public class ProjController {
	
	private final ProjService projService; 

	@Operation(summary = "프로젝트 목록", description = "프로젝트 목록")
	@GetMapping(value = {"/{compSeq}/project"})
	public ResponseEntity<ApiListResponse<List<ProjInfo>>> listProj(HttpServletRequest request
			, @PathVariable String compSeq
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("compSeq", compSeq);
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, projService.countProj(map)));
		
		List<ProjInfo> projList = projService.listProj(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<ProjInfo>>builder()
				.request(request)
				.data(projList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "프로젝트 정보", description = "프로젝트 정보")
    @GetMapping(value = "/{compSeq}/project/{projSeq}")
    public ResponseEntity<ApiResponse<ProjInfo>> getProjBySeq(HttpServletRequest request
    		, @PathVariable long compSeq
    		, @PathVariable long projSeq
			, @Parameter(hidden = true) Proj proj) {
    	
    	ProjInfo project = projService.getProjBySeq(proj);
    	
		return ResponseEntity.ok(ApiResponse.<ProjInfo>builder()
				.request(request)
				.data(project)
				.build());
    }
    
    @Operation(summary = "프로젝트 등록", description = "프로젝트 등록")
	@PostMapping(value = "/{compSeq}/project", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertProj(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @PathVariable long compSeq
			, @ModelAttribute @Valid Proj proj) {
    	
    	projService.insertProj(proj.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "프로젝트 수정", description = "프로젝트 수정")
	@PutMapping(value = "/{compSeq}/project/{projSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateProj(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long compSeq
			, @PathVariable long projSeq
			, @ModelAttribute @Valid Proj proj) {
    	
    	boolean result = projService.updateProj(proj.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제")
	@DeleteMapping(value = "/{compSeq}/project/{projSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteProj(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long compSeq
			, @PathVariable long projSeq
			, @Parameter(hidden = true) Proj proj) {
    	
    	boolean result = projService.deleteProj(proj.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
}
