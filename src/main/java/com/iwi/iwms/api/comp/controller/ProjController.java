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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.comp.domain.Proj;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.domain.ProjUser;
import com.iwi.iwms.api.comp.domain.ProjUserInfo;
import com.iwi.iwms.api.comp.domain.ProjUserList;
import com.iwi.iwms.api.comp.service.ProjService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Project", description = "IWMS 프로젝트 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/project")
public class ProjController {
	
	private final ProjService projService; 

	@Operation(summary = "프로젝트 목록", description = "프로젝트 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<ProjInfo>>> listProj(HttpServletRequest request
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
		map.put("pagination", new Pagination(page, limit, projService.countProj(map)));
		
		List<ProjInfo> projList = projService.listProj(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<ProjInfo>>builder()
				.request(request)
				.data(projList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "프로젝트 정보", description = "프로젝트 정보")
    @GetMapping(value = "/{projSeq}")
    public ResponseEntity<ApiResponse<ProjInfo>> getProjBySeq(HttpServletRequest request
    		, @PathVariable long projSeq) {
    	
    	ProjInfo project = projService.getProjBySeq(projSeq);
    	
		return ResponseEntity.ok(ApiResponse.<ProjInfo>builder()
				.request(request)
				.data(project)
				.build());
    }
    
    @Operation(summary = "프로젝트 등록", description = "프로젝트 등록")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertProj(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @ModelAttribute @Valid Proj proj) {
    	
    	projService.insertProj(proj.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "프로젝트 수정", description = "프로젝트 수정")
	@PutMapping(value = "/{projSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateProj(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long projSeq
			, @ModelAttribute @Valid Proj proj) {
    	
    	boolean result = projService.updateProj(proj.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제")
	@DeleteMapping(value = "/{projSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteProj(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long projSeq
			, @Parameter(hidden = true) Proj proj) {
    	
    	boolean result = projService.deleteProj(proj.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "프로젝트 담당자 목록 조회", description = "프로젝트 담당자 목록 조회")
	@GetMapping(value = "/{projSeq}/projUser")
	public ResponseEntity<ApiResponse<List<ProjUserInfo>>> listProjUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long projSeq
			, @Parameter(description = "구분: [1: 고객사, 2: 수행사]") @RequestParam(value = "projUserGb", required = true) int projUserGb) {
    	
    	List<ProjUserInfo> projUserList = null;
    			
		if(projUserGb == 1) {
			projUserList = projService.listCustProjUser(projSeq);
		} else if(projUserGb == 2) {
			projUserList = projService.listPerfProjUser(projSeq);
		}
    	
		return ResponseEntity.ok(ApiResponse.<List<ProjUserInfo>>builder()
				.request(request)
				.data(projUserList)
				.build());
	}
    
    @Operation(summary = "프로젝트 담당자 등록", description = "프로젝트 담당자 등록")
	@PostMapping(value = "/{projSeq}/projUser", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateProjUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long projSeq
			, @RequestBody @Valid ProjUserList projUserList) {
    	
    	projUserList.setProjSeq(projSeq);
    	boolean result = projService.updateProjUser(projUserList.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
}
