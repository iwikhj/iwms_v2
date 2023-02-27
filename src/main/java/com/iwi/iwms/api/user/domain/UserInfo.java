package com.iwi.iwms.api.user.domain;

import org.springframework.beans.BeanUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
	
	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "사용자 SEQ")
	private long userSeq;

	@Schema(description = "사용자ID")
	private String userId;
	
	@Schema(description = "사용자 이름")
	private String userNm;
	
	@Schema(description = "사용자 구분 코드: [00: 유지보수, 01: 전산담당, 02: 현업담당, 99: 관리자]") 
	private String userGbCd;
	
	@Schema(description = "사용자 구분")
	private String userGb;
	
	@Schema(description = "사용자 업무 코드: [PL: 기획, DS: 디자인, UI: 퍼블리싱, DV: 개발, ET: 기타]") 
	private String userBusiCd;
	
	@Schema(description = "사용자 업무")
	private String userBusi;

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
	
	@Schema(description = "전화번호") 
	private String userTel;
	
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
	
	@Schema(description = "비밀번호 초기화 여부") 
	private String pwdResetYn;
	
	@Schema(description = "로그인 실패 횟수") 
	private int loginErrCnt;
	
	@Schema(description = "검증 여부") 
	private String verifyYn;
	
	@Schema(description = "마지막 로그인 IP") 
	private String lastLoginIp;
	
	@Schema(description = "마지막 로그인 시간") 
	private String lastLoginDt;
	
	@Schema(description = "사용 여부") 
	private String useYn;
	
	@Schema(description = "계정 휴면 일자") 
	private String userDorYmd;
	
	@Schema(description = "계정 삭제 일자") 
	private String userDelYmd;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String updtDt;
	
	@Schema(description = "수정자") 
	private String updtNm;
	
	@Schema(description = "인증 서버 ID")
	private String ssoId;
	
	@Schema(description = "사용자 권한") 
	private String userRole;
	
	public User asUser() {
		User user = new User();
		BeanUtils.copyProperties(this, user);
		return user;
	}
}
