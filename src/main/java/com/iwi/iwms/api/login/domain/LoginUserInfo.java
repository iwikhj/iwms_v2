package com.iwi.iwms.api.login.domain;

import java.util.List;

import com.iwi.iwms.api.user.domain.MenuInfo;

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
	
	@Schema(description = "사용자 구분 코드") 
	private String userGbCd;
	
	@Schema(description = "사용자 업무 코드") 
	private String userBusiCd; 	

	@Schema(description = "직급 SEQ") 
	private long positionSeq;
	
	@Schema(description = "직급") 
	private String positionNm;
	
	@Schema(description = "직책 구분 코드") 
	private String dutyGbCd;
	
	@Schema(description = "소속 SEQ") 
	private long compSeq;
	
	@Schema(description = "소속 이름") 
	private String compNm;
	
	@Schema(description = "인증 서버 ID")
	private String ssoId;
	
	@Schema(description = "사용자 권한") 
	private String userRole;
	
	@Schema(description = "메뉴 목록")
	private List<MenuInfo> menus;
}
