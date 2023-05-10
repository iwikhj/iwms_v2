package com.iwi.iwms.api.menu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.auth.service.AuthService;
import com.iwi.iwms.api.code.domain.CodeInfo;
import com.iwi.iwms.api.code.service.CodeService;
import com.iwi.iwms.api.common.response.PageResponse;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.DeptInfo;
import com.iwi.iwms.api.comp.service.CompService;
import com.iwi.iwms.api.login.domain.LoginInfo;
import com.iwi.iwms.api.user.domain.UserSiteInfo;
import com.iwi.iwms.api.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Page popup", description = "IWMS 페이지별 팝업 정보")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.path}/popup")
public class PagePopupController {
	
	private final CodeService codeService;
	
	private final UserService userService;
	
	private final CompService compService;
	
	private final AuthService authService;
	
    @Operation(summary = "로그인 - 아이디 찾기 팝업", description = "아이디 찾기 팝업")
    @GetMapping(value = "/login/find-id")
    public ResponseEntity<PageResponse<Map<String, Object>>> findIdPopup(HttpServletRequest request) {
    	
		List<CompInfo> compList = compService.listComp(new HashMap<>());
    	
		Map<String, Object> data = new HashMap<>();
		data.put("compList", compList);
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.build());
    }
    
    @Operation(summary = "유지보수 - 유지보수 등록 및 수정 팝업", description = "유지보수 등록 및 수정 팝업")
    @GetMapping(value = "/maintain/request")
    public ResponseEntity<PageResponse<Map<String, Object>>> requestPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	List<UserSiteInfo> siteList = userService.listSiteByUserSeq(loginInfo.getUserSeq());
    	List<CodeInfo> reqGbCdList =  codeService.listCodeByUpCode("REQ_GB_CD");
    	List<CodeInfo> reqTypeCdList =  codeService.listCodeByUpCode("REQ_TYPE_CD");
    	
    	Map<String, Object> data = new HashMap<>();
    	data.put("siteList", siteList);
    	data.put("reqGbCdList", reqGbCdList);
    	data.put("reqTypeCdList", reqTypeCdList);

		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.build());
    }
    
    @Operation(summary = "유지보수 - 유지보수 작업 담당자 배정 팝업", description = "작업 담당자 배정 팝업")
    @GetMapping(value = "/maintain/task")
    public ResponseEntity<PageResponse<Map<String, Object>>> requestTaskPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	Map<String, Object> data = new HashMap<>();

		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.build());
    }   
  
    
    @Operation(summary = "시스템관리 - 사용자 등록 및 수정 팝업", description = "사용자 등록 및 수정 팝업")
    @GetMapping(value = "/system/user")
    public ResponseEntity<PageResponse<Map<String, Object>>> userPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
		
    	Map<String, Object> map = new HashMap<>();
    	
		List<CompInfo> compList = compService.listComp(map);
		List<DeptInfo> deptList = null;
		
    	if(!CollectionUtils.isEmpty(compList)) {
    		map.put("compSeq", compList.get(0).getCompSeq());
    		deptList = compService.listDept(map);
    	}
    	
    	List<CodeInfo> authCdList = authService.listAuth(map).stream()
	    		.map(v -> CodeInfo.builder()
	    					.codeCd(v.getAuthCd())
	    					.codeNm(v.getAuthNm())
	    					.build())
	    		.collect(Collectors.toList());
    	
    	List<CodeInfo> userGbCdList =  codeService.listCodeByUpCode("USER_GB_CD");
    	List<CodeInfo> busiRollCdList =  codeService.listCodeByUpCode("BUSI_ROLL_CD");
    	
    	Map<String, Object> data = new HashMap<>();
    	data.put("compList", compList);
    	data.put("deptList", deptList);
    	data.put("authCdList", authCdList);
    	data.put("userGbCdList", userGbCdList);
    	data.put("busiRollCdList", busiRollCdList);
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.build());
    }
    
    @Operation(summary = "시스템관리 - 소속 등록 및 수정 팝업", description = "소속 등록 및 수정 팝업")
    @GetMapping(value = "/system/company")
    public ResponseEntity<PageResponse<Map<String, Object>>> companyPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
    	List<CodeInfo> compGbCdList =  codeService.listCodeByUpCode("COMP_GB_CD");
    	
    	Map<String, Object> data = new HashMap<>();
    	data.put("compGbCdList", compGbCdList);
    	
		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.build());
    }
    
    
    @Operation(summary = "시스템관리 - 프로젝트 등록 및 수정 팝업", description = "프로젝트 등록 및 수정 팝업")
    @GetMapping(value = "/system/project")
    public ResponseEntity<PageResponse<Map<String, Object>>> projPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginInfo loginInfo) {
    	
       	List<CompInfo> compList = compService.listComp(new HashMap<>());
    		
    	Map<String, Object> data = new HashMap<>();
    	data.put("compList", compList);

		return ResponseEntity.ok(PageResponse.<Map<String, Object>>builder()
				.data(data)
				.build());
    }
    
}
