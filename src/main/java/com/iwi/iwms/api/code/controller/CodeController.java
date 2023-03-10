package com.iwi.iwms.api.code.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.code.domain.Code;
import com.iwi.iwms.api.code.domain.CodeInfo;
import com.iwi.iwms.api.code.service.CodeService;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.login.domain.LoginUserInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Common Code", description = "IWMS 공통 코드 관리")
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
@RequestMapping("${app.root}/${app.version}/codes")
public class CodeController {

	private final CodeService codeService;
	
    @Operation(summary = "코드 목록", description = "전체 코드 목록")
    @GetMapping(value = "")
    public ResponseEntity<ApiResponse<List<CodeInfo>>> listAllCode(HttpServletRequest request
    		, @Parameter(description = "코드 또는 코드 이름으로 검색") @RequestParam(value = "search", required = false) String search) {
    	
    	Map<String, Object> map = new HashMap<>();
    	map.put("search", search);
    	
    	List<CodeInfo> codeList = codeService.listCode(map);
    	
		return ResponseEntity.ok(ApiResponse.<List<CodeInfo>>builder()
				.request(request)
				.data(codeList)
				.build());
    }
    
    @Operation(summary = "코드 등록", description = "코드 등록")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> insertCode(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @ModelAttribute @Valid Code code) {
    	
    	codeService.insertCode(code.of(loginUserInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(true)
				.build());
	}
    
    @Operation(summary = "코드 수정", description = "코드 수정", hidden = true)
	@PutMapping(value = "/{codeSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> updateCode(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long codeSeq
			, @ModelAttribute @Valid Code code) {
    	
    	boolean result = codeService.updateCode(code.of(loginUserInfo)) > 0 ? true : false;
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
    @Operation(summary = "코드 삭제", description = "코드 삭제", hidden = true)
	@DeleteMapping(value = "/{codeSeq}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<ApiResponse<Boolean>> deleteCode(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long codeSeq
			, @ModelAttribute @Valid Code code) {
    	
    	boolean result = codeService.deleteCode(code.of(loginUserInfo)) > 0 ? true : false;
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
}
