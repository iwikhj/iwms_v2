package com.iwi.iwms.api.comp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.Dept;
import com.iwi.iwms.api.comp.domain.DeptInfo;
import com.iwi.iwms.api.comp.service.CompService;
import com.iwi.iwms.api.login.domain.LoginInfo;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Company", description = "IWMS 소속 관리")
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
@RequestMapping("${app.path}/companies")
public class CompController {
	
	private final CompService compService; 

	@Operation(summary = "소속 목록", description = "소속 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<CompInfo>>> listComp(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "size", required = false, defaultValue = "10") int size
			, @RequestParam(value = "keykind", required = false) String keykind
			, @RequestParam(value = "keyword", required = false) String keyword) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("keykind", keykind); 
		map.put("keyword", keyword); 
		map.put("loginUserSeq", loginInfo.getUserSeq()); 
		map.put("pagination", new Pagination(page, size, compService.countComp(map)));
		List<CompInfo> compList = compService.listComp(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<CompInfo>>builder()
				.data(compList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "소속 상세 정보", description = "소속 상세 정보")
    @GetMapping(value = "/{compSeq}")
    public ResponseEntity<ApiResponse<CompInfo>> getCompBySeq(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo
    		, @PathVariable long compSeq) {
    	
    	CompInfo compInfo = compService.getCompBySeq(compSeq, loginInfo.getUserSeq());
    	
		return ResponseEntity.ok(ApiResponse.<CompInfo>builder()
				.data(compInfo)
				.build());
    }
    
    @Operation(summary = "소속 등록", description = "소속 등록")
	@PostMapping(value = "")
	public ResponseEntity<ApiResponse<Boolean>> insertComp(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @Valid Comp comp) {
    	
    	compService.insertComp(comp.of(loginInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "소속 수정", description = "소속 수정")
	@PutMapping(value = "/{compSeq}")
	public ResponseEntity<ApiResponse<Boolean>> updateComp(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long compSeq
			, @Valid Comp comp) {
    	
    	boolean result = compService.updateComp(comp.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "소속 삭제", description = "소속 삭제")
	@DeleteMapping(value = "/{compSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteComp(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long compSeq
			, @Parameter(hidden = true) Comp comp) {
    	
    	boolean result = compService.deleteComp(comp.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
	@Operation(summary = "소속 부서 목록", description = "소속 부서 목록")
	@GetMapping(value = "/{compSeq}/depts")
	public ResponseEntity<ApiResponse<List<DeptInfo>>> listDept(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo
			, @PathVariable long compSeq) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("compSeq", compSeq);
		map.put("loginUserSeq", loginInfo.getUserSeq()); 
		List<DeptInfo> deptList = compService.listDept(map);
    	
		return ResponseEntity.ok(ApiResponse.<List<DeptInfo>>builder()
				.data(deptList)
				.build());
	}
	
    @Operation(summary = "소속 부서 상세 정보", description = "소속 부서 상세 정보")
    @GetMapping(value = "/{compSeq}/depts/{deptSeq}")
    public ResponseEntity<ApiResponse<DeptInfo>> getDeptBySeq(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo
    		, @PathVariable long compSeq
    		, @PathVariable long deptSeq) {
    	
    	DeptInfo deptInfo = compService.getDeptBySeq(deptSeq, loginInfo.getUserSeq());
    	
		return ResponseEntity.ok(ApiResponse.<DeptInfo>builder()
				.data(deptInfo)
				.build());
    }	
    
    @Operation(summary = "소속 부서 등록", description = "소속 부서 등록")
	@PostMapping(value = "/{compSeq}/depts")
	public ResponseEntity<ApiResponse<Boolean>> insertDept(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo
    		, @PathVariable long compSeq
			, @Valid Dept dept) {
    	
    	compService.insertDept(dept.of(loginInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "소속 부서 수정", description = "소속 부서 수정")
	@PutMapping(value = "/{compSeq}/depts/{deptSeq}")
	public ResponseEntity<ApiResponse<Boolean>> updateDept(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long compSeq
			, @PathVariable long deptSeq
			, @Valid Dept dept) {
    	
    	boolean result = compService.updateDept(dept.of(loginInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "소속 부서 삭제", description = "소속 부서 삭제")
	@DeleteMapping(value = "/{compSeq}/depts/{deptSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteDept(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long compSeq
			, @PathVariable long deptSeq
			, @Parameter(hidden = true) Dept dept) {
    	
    	boolean result = compService.deleteDept(dept.of(loginInfo)) > 0 ? true : false;
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
}
