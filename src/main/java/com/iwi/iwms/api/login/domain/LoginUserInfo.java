package com.iwi.iwms.api.login.domain;

import java.util.List;
import java.util.stream.Collectors;

import com.iwi.iwms.api.auth.domain.AuthMenuInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserInfo {
	
	@Schema(description = "사용자 SEQ")
	private long userSeq;

	@Schema(description = "사용자ID")
	private String userId;
	
	@Schema(description = "사용자 이름")
	private String userNm;

	@Schema(description = "사용자 이메일")
	private String userEmail;
	
	@Schema(description = "사용자 권한") 
	private String authCd;
	
	@Schema(description = "사용자 권한 이름") 
	private String authNm;
	
	@Schema(description = "사용자 구분 코드") 
	private String userGbCd;
	
	@Schema(description = "사용자 구분") 
	private String userGb;
	
	@Schema(description = "사용자 업무 코드")
	private String busiRollCd;
	
	@Schema(description = "사용자 업무")
	private String busiRoll; 	
	
	@Schema(description = "부서") 
	private String deptNm;
	
	@Schema(description = "직급(직책)") 
	private String posiNm;
	
	@Schema(description = "소속 SEQ") 
	private long compSeq;
	
	@Schema(description = "소속 이름") 
	private String compNm;
	
	@Schema(description = "로그인 IP") 
	private String loginIp;
	
	@Schema(description = "로그인 시간") 
	private String loginDt;
	
	@Schema(description = "메뉴 목록")
	private List<AuthMenuInfo> menus;
	
	public void setMenuSelected(String pages, String uri) {
		//Mapping이 pages가 아니면 무시
    	if(uri.indexOf(pages) != -1) {
    		
    		//전체 URI에서 /pages까지 경로 제거
    	   	String page = uri.replaceFirst(pages, "");
    	   	
    	   	//URI에서 /detail이후 경로 제외
    	   	page = page.substring(0, page.lastIndexOf("/detail") == -1 ? page.length() : page.lastIndexOf("/detail"));
    	   	
    	   	//gnb menu 추출
    	   	String gnbMenu = page.lastIndexOf("/") == 0 ? page : page.substring(0, page.lastIndexOf("/"));
    	   	
    	   	//lnb menu 추출
    		String lnbMenu = page;
    	   	
    	   	this.getMenus().stream()
	    	   		.filter(v -> v.getPageUrl().equals(gnbMenu))
	    	   		.map(v -> {
	    	   			v.setSelectedYn("Y");
	    	   			v.getSubMenus().stream()
	    	   				.filter(s -> s.getPageUrl().equals(lnbMenu))
	    	   				.map(s -> {s.setSelectedYn("Y"); return s;})
	    	   				.collect(Collectors.toList());    	   			
	    	   			return v;
	    	   		})
	    	   		.collect(Collectors.toList());
    	}	
	}
}
