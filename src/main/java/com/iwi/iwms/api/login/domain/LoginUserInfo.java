package com.iwi.iwms.api.login.domain;

import java.util.List;

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
	
	@Schema(description = "사용자 구분") 
	private String userGb;
	
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
}
