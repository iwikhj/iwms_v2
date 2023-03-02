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

	@Email(message = "사용자 아이디는 이메일 형식으로 입력해주세요")
	@NotNull(message = "사용자 아이디는 필수 입력 사항입니다")
	@Schema(description = "사용자 아이디(이메일 형식)")
	private String userId;
	
	@NotNull(message = "사용자 이름은 필수 입력 사항입니다")
	@Schema(description = "사용자 이름")
	private String userNm;
	
	@NotNull
	@Schema(description = "사용자 구분 코드: [00: 유지보수, 01: 전산담당, 02: 현업담당, 99: 관리자]", allowableValues = {"00", "01", "02", "99"}) 
	private String userGbCd;
	
	@NotNull
	@Schema(description = "사용자 업무 코드: [PL: 기획, DS: 디자인, UI: 퍼블리싱, DV: 개발, ET: 기타]", allowableValues = {"PL", "DS", "UI", "DV", "ET"}) 
	private String userBusiCd; 

	@NotNull(message = "직급은 필수 입력 사항입니다")
	@Schema(description = "직급 SEQ") 
	private long positionSeq;
	
	@Schema(description = "직책 구분 코드") 
	private String dutyGbCd;
	
	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(description = "소속 SEQ") 
	private long compSeq;
	
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
	
	@Schema(description = "사용자 CI") 
	private String userCiKey;
	
	@Schema(description = "사용자 DI") 
	private String userDiKey;
	
	@Schema(hidden = true, description = "비밀번호 초기화 여부", allowableValues = {"Y", "N"}) 
	private String pwdResetYn;
	
	@NotNull
	@Schema(description = "검증 여부", defaultValue = "N", allowableValues = {"Y", "N"}) 
	private String verifyYn;
	
	@Schema(hidden = true, description = "마지막 로그인 IP") 
	private String lastLoginIp;
	
	@NotNull
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "인증 서버 ID")
	private String ssoId;
	
	@NotNull
	@Schema(description = "사용자 권한", defaultValue = "ROLE_IWMS_USER", allowableValues = {"ROLE_IWMS_ADMIN", "ROLE_IWMS_USER", "ROLE_IWMS_DEV"}) 
	private String userRole;

	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public User of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
	
}
