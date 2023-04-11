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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.auth.service.AuthService;
import com.iwi.iwms.api.code.domain.CodeInfo;
import com.iwi.iwms.api.code.service.CodeService;
import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.DeptInfo;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.service.CompService;
import com.iwi.iwms.api.comp.service.ProjService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.notice.service.NoticeService;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.service.ReqDtlService;
import com.iwi.iwms.api.req.service.ReqService;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.domain.UserSiteInfo;
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
@RequestMapping("${app.path}/${app.version}/popup")
public class PagePopupController {
	
	private static final int DEFAULT_PAGE = 1;
	
	private static final int DEFAULT_LIMIT = 15;
	
	private final CodeService codeService;
	
	private final NoticeService noticeService;
	
	private final ReqService reqService;
	
	private final ReqDtlService reqDtlService;
	
	private final ProjService projService;
	
	private final UserService userService;
	
	private final CompService compService;
	
	private final AuthService authService;
	
    @Operation(summary = "로그인 - 아이디 찾기 팝업", description = "아이디 찾기 팝업")
    @GetMapping(value = "/login/find-id")
    public ResponseEntity<ApiResponse<Void>> findIdPopup(HttpServletRequest request) {
    	
    	//참조 데이터
    	Map<String, Object> ref = new HashMap<>();
    	
    	Map<String, Object> map = new HashMap<>();
    	map.put("useYn", "Y");
		List<CompInfo> compList = compService.listComp(map);
    	
    	ref.put("compList", compList);

		return ResponseEntity.ok(ApiResponse.<Void>builder()
				.ref(ref)
				.build());
    }
    
    @Operation(summary = "유지보수 - 유지보수 등록 및 수정 팝업", description = "유지보수 등록 및 수정 팝업")
    @GetMapping(value = "/maintain/request")
    public ResponseEntity<ApiResponse<ReqInfo>> requestPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @RequestParam(value = "seq", required = false) Long reqSeq) {
    	
    	ReqInfo reqInfo = null;
    	
    	if(reqSeq != null) {
    		reqInfo = reqService.getReqBySeq(reqSeq, loginUserInfo.getUserSeq());
    	}
    	
    	//참조 데이터
    	Map<String, Object> ref = new HashMap<>();
    	
    	List<UserSiteInfo> siteList = userService.listSiteByUserSeq(loginUserInfo.getUserSeq());
    	List<CodeInfo> reqGbCdList =  codeService.listCodeByUpCode("REQ_GB_CD");
    	List<CodeInfo> reqTypeCdList =  codeService.listCodeByUpCode("REQ_TYPE_CD");
    	
    	ref.put("siteList", siteList);
    	ref.put("reqGbCdList", reqGbCdList);
    	ref.put("reqTypeCdList", reqTypeCdList);

		return ResponseEntity.ok(ApiResponse.<ReqInfo>builder()
				.data(reqInfo)
				.ref(ref)
				.build());
    }
    
    @Operation(summary = "유지보수 - 유지보수 작업 담당자 배정 팝업", description = "작업 담당자 배정 팝업")
    @GetMapping(value = "/maintain/task")
    public ResponseEntity<ApiResponse<ReqInfo>> requestTaskPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @RequestParam(value = "seq", required = true) long reqSeq) {
    	
    	ReqInfo reqInfo = reqService.getReqBySeq(reqSeq, loginUserInfo.getUserSeq());
    	
    	//참조 데이터
    	Map<String, Object> ref = new HashMap<>();
    	
    	List<DeptInfo> deptList = reqService.listDeptForTask(reqInfo.getProjSeq());
    	
    	ref.put("deptList", deptList);

		return ResponseEntity.ok(ApiResponse.<ReqInfo>builder()
				.data(reqInfo)
				.ref(ref)
				.build());
    }   
  
    
    @Operation(summary = "시스템관리 - 사용자 등록 및 수정 팝업", description = "사용자 등록 및 수정 팝업")
    @GetMapping(value = "/system/user")
    public ResponseEntity<ApiResponse<UserInfo>> userPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @RequestParam(value = "seq", required = false) Long userSeq) {
    	
    	UserInfo userInfo = null;
    	
    	if(userSeq != null) {
    		userInfo = userService.getUserBySeq(userSeq, loginUserInfo.getUserSeq());
    	}
		
    	//참조 데이터
    	Map<String, Object> ref = new HashMap<>();
    	
    	Map<String, Object> map = PredicateMap.make(request, loginUserInfo);
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
    	
    	ref.put("compList", compList);
    	ref.put("deptList", deptList);
    	ref.put("authCdList", authCdList);
    	ref.put("userGbCdList", userGbCdList);
    	ref.put("busiRollCdList", busiRollCdList);
    	
		return ResponseEntity.ok(ApiResponse.<UserInfo>builder()
				.data(userInfo)
				.ref(ref)
				.build());
    }
    
    @Operation(summary = "시스템관리 - 소속 등록 및 수정 팝업", description = "소속 등록 및 수정 팝업")
    @GetMapping(value = "/system/company")
    public ResponseEntity<ApiResponse<CompInfo>> companyPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @RequestParam(value = "seq", required = false) Long compSeq) {
    	
    	CompInfo compInfo = null;
    	
    	if(compSeq != null) {
    		compInfo = compService.getCompBySeq(compSeq, loginUserInfo.getUserSeq());
    	}
    	
    	//참조 데이터
    	Map<String, Object> ref = new HashMap<>();
    	
    	List<CodeInfo> compGbCdList =  codeService.listCodeByUpCode("COMP_GB_CD");
    	
    	ref.put("compGbCdList", compGbCdList);
    	
		return ResponseEntity.ok(ApiResponse.<CompInfo>builder()
				.data(compInfo)
				.ref(ref)
				.build());
    }
    
    
    @Operation(summary = "시스템관리 - 프로젝트 등록 및 수정 팝업", description = "프로젝트 등록 및 수정 팝업")
    @GetMapping(value = "/system/project")
    public ResponseEntity<ApiResponse<ProjInfo>> projPopup(HttpServletRequest request
    		, @Parameter(hidden = true) LoginUserInfo loginUserInfo
    		, @RequestParam(value = "seq", required = false) Long projSeq) {
    	
    	ProjInfo projInfo = null;
    	
    	if(projSeq != null) {
    		projInfo = projService.getProjBySeq(projSeq, loginUserInfo.getUserSeq());
    	}
    	
    	//참조 데이터
    	Map<String, Object> ref = new HashMap<>();
    	
    	Map<String, Object> map = PredicateMap.make(request, loginUserInfo);
		List<CompInfo> compList = compService.listComp(map);
    	
    	ref.put("compList", compList);

		return ResponseEntity.ok(ApiResponse.<ProjInfo>builder()
				.data(projInfo)
				.ref(ref)
				.build());
    }
    
}
