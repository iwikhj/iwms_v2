package com.iwi.iwms.api.code.controller;

import java.util.List;

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

import com.iwi.iwms.api.code.domain.Code;
import com.iwi.iwms.api.code.domain.CodeInfo;
import com.iwi.iwms.api.code.service.CodeService;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.login.domain.LoginInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Common Code", description = "IWMS 공통 코드 관리")
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('ROLE_IWMS_ADMIN')")
@RequestMapping("${app.path}/codes")
public class CodeController {

	private final CodeService codeService;
	
    @Operation(summary = "코드 목록 조회", description = "코드 목록 조회")
    @GetMapping(value = "")
    public ResponseEntity<ApiResponse<List<CodeInfo>>> listCode(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo
    		, @RequestParam(value = "code", required = false) String code) {
    	
    	//Map<String, Object> map = PredicateMap.make(request, loginInfo);
    	List<CodeInfo> codeList = codeService.listCodeByUpCode(code);
    	
		return ResponseEntity.ok(ApiResponse.<List<CodeInfo>>builder()
				.data(codeList)
				.build());
    }
    
    @Operation(hidden = true, summary = "코드 등록", description = "코드 등록")
	@PostMapping(value = "")
	public ResponseEntity<ApiResponse<Boolean>> insertCode(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @Valid Code code) {
    	
    	codeService.insertCode(code.of(loginInfo));

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(true)
				.build());
	}
    
    @Operation(summary = "코드 수정", description = "코드 수정", hidden = true)
	@PutMapping(value = "/{codeSeq}")
	public ResponseEntity<ApiResponse<Boolean>> updateCode(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long codeSeq
			, @Valid Code code) {
    	
    	boolean result = codeService.updateCode(code.of(loginInfo)) > 0 ? true : false;
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
    
    @Operation(summary = "코드 삭제", description = "코드 삭제", hidden = true)
	@DeleteMapping(value = "/{codeSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteCode(HttpServletRequest request
			, @Parameter(hidden = true) LoginInfo loginInfo		
			, @PathVariable long codeSeq
			, @Parameter(hidden = true) Code code) {
    	
    	boolean result = codeService.deleteCode(code.of(loginInfo)) > 0 ? true : false;
    	
		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.data(result)
				.build());
	}
}
