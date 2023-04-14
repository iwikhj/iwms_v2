package com.iwi.iwms.api.comp.controller;

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
import com.iwi.iwms.api.comp.domain.ProjUser;
import com.iwi.iwms.api.comp.domain.ProjUserInfo;
import com.iwi.iwms.api.comp.domain.Site;
import com.iwi.iwms.api.comp.domain.SiteInfo;
import com.iwi.iwms.api.comp.service.ProjService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.utils.Pagination;
import com.iwi.iwms.utils.PredicateMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Project", description = "IWMS 프로젝트 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.path}/projects")
public class ProjController {
	
	private final ProjService projService; 

	@Operation(summary = "프로젝트 목록", description = "프로젝트 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<ProjInfo>>> listProj(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "compSeq", required = false) Long compSeq
			, @RequestParam(value = "projNm", required = false) String projNm
			, @RequestParam(value = "projStdYmd", required = false) String projStdYmd
			, @RequestParam(value = "projEndYmd", required = false) String projEndYmd
			, @RequestParam(value = "useYn", required = false) String useYn) {
		
		Map<String, Object> map = PredicateMap.make(request, loginUserInfo);
		map.put("pagination", new Pagination(page, limit, projService.countProj(map)));
		List<ProjInfo> projList = projService.listProj(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<ProjInfo>>builder()
				.data(projList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "프로젝트 상세 정보", description = "프로젝트 상세 정보")
    @GetMapping(value = "/{projSeq}")
    public ResponseEntity<ApiResponse<ProjInfo>> getProjBySeq(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @PathVariable long projSeq) {
    	
    	ProjInfo projInfo = projService.getProjBySeq(projSeq, loginUserInfo.getUserSeq());
    	
		return ResponseEntity.ok(ApiResponse.<ProjInfo>builder()
				.data(projInfo)
				.build());
    }
    
    @Operation(summary = "프로젝트 등록", description = "프로젝트 등록")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertProj(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @ModelAttribute @Valid Proj proj) {
    	
    	projService.insertProj(proj.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
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
				.data(result)
				.build());
	}
    
    @Operation(summary = "프로젝트 담당자 목록 조회", description = "프로젝트 담당자 목록 조회")
	@GetMapping(value = "/{projSeq}/users")
	public ResponseEntity<ApiResponse<List<ProjUserInfo>>> listProjUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long projSeq
			, @Parameter(description = "담당자 구분: [1:수행사, 2:고객사]") @RequestParam(value = "projUserGb", required = true) int projUserGb) {
    	
    	List<ProjUserInfo> projUserList = null;
    			
		if(projUserGb == 1) {
			projUserList = projService.listPerfProjUser(projSeq);
		} else if(projUserGb == 2) {
			projUserList = projService.listCustProjUser(projSeq);
		}
    	
		return ResponseEntity.ok(ApiResponse.<List<ProjUserInfo>>builder()
				.data(projUserList)
				.build());
	}
    
    @Operation(summary = "프로젝트 담당자 등록", description = "프로젝트 담당자 등록")
	@PostMapping(value = "/{projSeq}/users", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateProjUser(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long projSeq
			, @ModelAttribute @Valid ProjUser projUser) {
    	
    	boolean result = projService.updateProjUser(projUser.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
	@Operation(summary = "사이트 목록", description = "사이트 목록")
	@GetMapping(value = "/{projSeq}/sites")
	public ResponseEntity<ApiListResponse<List<SiteInfo>>> listSite(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long projSeq
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "useYn", required = false) String useYn) {
		
		Map<String, Object> map = PredicateMap.make(request, loginUserInfo);
		map.put("projSeq", projSeq);
		map.put("pagination", new Pagination(page, limit, projService.countSite(map)));
		List<SiteInfo> SiteList = projService.listSite(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<SiteInfo>>builder()
				.data(SiteList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "사이트 상세 정보", description = "사이트 상세 정보")
    @GetMapping(value = "/{projSeq}/sites/{siteSeq}")
    public ResponseEntity<ApiResponse<SiteInfo>> getSiteBySeq(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @PathVariable long projSeq
    		, @PathVariable long siteSeq) {
    	
    	SiteInfo siteInfo = projService.getSiteBySeq(siteSeq, loginUserInfo.getUserSeq());
    	
		return ResponseEntity.ok(ApiResponse.<SiteInfo>builder()
				.data(siteInfo)
				.build());
    }
    
    @Operation(summary = "사이트 등록", description = "사이트 등록")
	@PostMapping(value = "/{projSeq}/sites", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertSite(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @PathVariable long projSeq
			, @ModelAttribute @Valid Site site) {
    	
    	projService.insertSite(site.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "사이트 수정", description = "사이트 수정")
	@PutMapping(value = "/{projSeq}/sites/{siteSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateSite(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long projSeq
			, @PathVariable long siteSeq
			, @ModelAttribute @Valid Site site) {
    	
    	boolean result = projService.updateSite(site.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "사이트 삭제", description = "사이트 삭제")
	@DeleteMapping(value = "/{projSeq}/sites/{siteSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteSite(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long projSeq
			, @PathVariable long siteSeq
			, @Parameter(hidden = true) Site site) {
    	
    	boolean result = projService.deleteSite(site.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}    
}
