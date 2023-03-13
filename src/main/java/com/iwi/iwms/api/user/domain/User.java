package com.iwi.iwms.api.user.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.iwi.iwms.api.login.domain.LoginUserInfo;

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
public class User {
	
	@Schema(hidden = true, description = "사용자 SEQ")
	private long userSeq;

	@NotNull(message = "사용자 아이디는 필수 입력 사항입니다")
	@Schema(description = "사용자 아이디")
	private String userId;
	
	@NotNull(message = "사용자 이름은 필수 입력 사항입니다")
	@Schema(description = "사용자 이름")
	private String userNm;
	
	@Email(message = "사용자 이메일이 형식에 맞지 않습니다.")
	@NotNull(message = "사용자 이메일은 필수 입력 사항입니다")
	@Schema(description = "사용자 이메일")
	private String userEmail;	
	
	@Schema(hidden = true, description = "인증 서버 키")
	private String ssoKey;
	
	@Schema(hidden = true, description = "권한 SEQ") 
	private Long authSeq;
	
	@NotNull(message = "권한은 필수 입력 사항입니다")
	@Schema(description = "권한: [ADMIN: 최고관리자, PM: PM, PL: PL, ENG: 작업자, CM: 현업담당자, USER: 일반사용자]", allowableValues = {"ADMIN", "PM", "PL", "ENG", "CM", "USER"}) 
	private String authCd;	
	
	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(description = "소속 SEQ") 
	private Long compSeq;
	
	@Schema(description = "부서 SEQ") 
	private Long deptSeq;
	
	@Schema(description = "직급(직책)")
	private String posiNm;
	
	@Schema(description = "사용자 구분 코드: [00:관리자, 01:유지보수, 10:전산담당, 11:현업담당]", allowableValues = {"00", "01", "10", "11"}) 
	private String userGbCd;
	
	@Schema(description = "사용자 업무 코드: [PN:기획, DS:디자인, UI:퍼블리싱, DV:개발, DB:DB, ET:기타]", allowableValues = {"PN", "DS", "UI", "DV", "DB", "ET"}) 
	private String busiRollCd; 
	
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호가 아닙니다")
	@Schema(description = "전화번호") 
	private String userTel;
	
	@Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "유효한 핸드폰번호가 아닙니다")
	@Schema(description = "핸드폰번호") 
	private String userHp;
	
	@Schema(description = "우편번호") 
	private String userPostNo;
	
	@Schema(description = "주소") 
	private String userAddr;
	
	@Schema(description = "상세 주소") 
	private String userDtlAddr;
	
	@Schema(description = "검증 여부", allowableValues = {"Y", "N"}) 
	private String verifyYn;
	
	@Schema(hidden = true, description = "마지막 로그인 IP") 
	private String lastLoginIp;
	
	@Schema(description = "사용 여부", allowableValues = {"Y", "N"}) 
	private String useYn;

	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long uptSeq;
	
	public User of(final LoginUserInfo loginUserInfo) {
		this.authCd = "ROLE_IWMS_" + this.authCd;
		this.regSeq = loginUserInfo.getUserSeq();
		this.uptSeq = loginUserInfo.getUserSeq();
		return this;
	}
	
}
