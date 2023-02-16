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

import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.Position;
import com.iwi.iwms.api.comp.domain.PositionInfo;
import com.iwi.iwms.api.comp.service.CompService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Company", description = "IWMS 소속 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/company")
public class CompController {
	
	private final CompService compService; 

	@Operation(summary = "소속 목록", description = "소속 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<CompInfo>>> listComp(HttpServletRequest request
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, compService.countComp(map)));
		
		List<CompInfo> compList = compService.listComp(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<CompInfo>>builder()
				.request(request)
				.data(compList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "소속 정보", description = "소속 정보")
    @GetMapping(value = "/{compSeq}")
    public ResponseEntity<ApiResponse<CompInfo>> getCompBySeq(HttpServletRequest request
    		, @PathVariable long compSeq) {
    	
    	CompInfo comp = compService.getCompBySeq(compSeq);
    	
		return ResponseEntity.ok(ApiResponse.<CompInfo>builder()
				.request(request)
				.data(comp)
				.build());
    }
    
    @Operation(summary = "소속 등록", description = "소속 등록")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertComp(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @ModelAttribute @Valid Comp comp) {
    	
    	compService.insertComp(comp.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "소속 수정", description = "소속 수정")
	@PutMapping(value = "/{compSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateComp(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long compSeq
			, @ModelAttribute @Valid Comp comp) {
    	
    	boolean result = compService.updateComp(comp.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "소속 삭제", description = "소속 삭제")
	@DeleteMapping(value = "/{compSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteComp(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long compSeq
			, @Parameter(hidden = true) Comp comp) {
    	
    	boolean result = compService.deleteComp(comp.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
	@Operation(summary = "소속 직급 목록", description = "소속 직급 목록")
	@GetMapping(value = "/{compSeq}/position")
	public ResponseEntity<ApiListResponse<List<PositionInfo>>> listPosition(HttpServletRequest request
			, @PathVariable long compSeq
			, @RequestParam(value = "search", required = false) String search) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("compSeq", compSeq);
		map.put("search", search);
		
		List<PositionInfo> positionList = compService.listPosition(map);

		return ResponseEntity.ok(ApiListResponse.<List<PositionInfo>>builder()
				.request(request)
				.data(positionList)
				.query(map)
				.build());
	}
    
    @Operation(summary = "소속 직급 등록", description = "소속 직급 등록")
	@PostMapping(value = "/{compSeq}/position", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertPosition(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @PathVariable long compSeq
			, @ModelAttribute @Valid Position position) {
    	
    	compService.insertPosition(position.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "소속 직급 수정", description = "소속 직급 수정")
	@PutMapping(value = "/{compSeq}/position/{positionSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updatePosition(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long compSeq
			, @PathVariable long positionSeq
			, @ModelAttribute @Valid Position position) {
    	
    	boolean result = compService.updatePosition(position.of(loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "소속 직급 삭제", description = "소속 직급 삭제")
	@DeleteMapping(value = "/{compSeq}/position/{positionSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deletePosition(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long compSeq
			, @PathVariable long positionSeq
			, @Parameter(hidden = true) Position position) {
    	
    	boolean result = compService.deletePosition(position.of(loginUserInfo)) > 0 ? true : false;
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
}
