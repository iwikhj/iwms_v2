package com.iwi.iwms.api.menu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.auth.service.AuthService;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.DeptInfo;
import com.iwi.iwms.api.comp.service.CompService;
import com.iwi.iwms.api.comp.service.ProjService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.notice.service.NoticeService;
import com.iwi.iwms.api.req.service.ReqDtlService;
import com.iwi.iwms.api.req.service.ReqService;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.service.UserService;
import com.iwi.iwms.utils.PredicateMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Page popup", description = "IWMS 페이지별 팝업 정보")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.root}/${app.version}/popup")
public class MenuPopupController {
	
	private static final int DEFAULT_PAGE = 1;
	
	private static final int DEFAULT_LIMIT = 15;
	
	private final NoticeService noticeService;
	
	private final ReqService reqService;
	
	private final ReqDtlService reqDtlService;
	
	private final ProjService projService;
	
	private final UserService userService;
	
	private final CompService compService;
	
	private final AuthService authService;
  
    
    @Operation(summary = "시스템관리 - 사용자 등록 및 수정 팝업", description = "사용자 등록 및 수정 팝업")
    @GetMapping(value = "/system/user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> systemUserPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @RequestParam(value = "seq", required = false) Long userSeq) {
    	
    	Map<String, Object> resultData = new HashMap<>();
    	
    	if(userSeq != null) {
    		UserInfo userInfo = userService.getUserBySeq(userSeq, loginUserInfo.getUserSeq());
    		resultData.put("userInfo", userInfo);
    	}
		
    	Map<String, Object> map = PredicateMap.make(request, loginUserInfo);
    	map.put("useYn", "Y");
    	
		List<CompInfo> compList = compService.listComp(map);
    	resultData.put("compList", compList);
    	
    	if(!CollectionUtils.isEmpty(compList) && compList.size() > 0) {
    		map.put("compSeq", compList.get(0).getCompSeq());
    		List<DeptInfo> deptList = compService.listDept(map);
        	resultData.put("deptList", deptList);
    	}
    	
		return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
				.data(resultData)
				.build());
    }
    
    @Operation(summary = "시스템관리 - 소속 등록 및 수정 팝업", description = "소속 등록 및 수정 팝업")
    @GetMapping(value = "/system/company")
    public ResponseEntity<ApiResponse<Map<String, Object>>> systemCompanyPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @RequestParam(value = "seq", required = false) Long compSeq) {
    	
    	Map<String, Object> resultData = new HashMap<>();
    	
    	if(compSeq != null) {
    		CompInfo compInfo = compService.getCompBySeq(compSeq, loginUserInfo.getUserSeq());
    		resultData.put("compInfo", compInfo);
    	}
    	
		return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
				.data(resultData)
				.build());
    }
    
    
}
