package com.iwi.iwms.api.user.domain;

import com.iwi.iwms.api.file.domain.UploadFileInfo;

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

	@Schema(description = "사용자 아이디")
	private String userId;
	
	@Schema(description = "사용자 이름")
	private String userNm;
	
	@Schema(description = "사용자 이메일")
	private String userEmail;
	
	@Schema(description = "인증 서버 키") 
	private String ssoKey;	
	
	@Schema(description = "사용자 권한 SEQ") 
	private long authSeq;
	
	@Schema(description = "사용자 권한") 
	private String authCd;
	
	@Schema(description = "사용자 권한 이름") 
	private String authNm;
	
	@Schema(description = "소속 SEQ") 
	private long compSeq;
	
	@Schema(description = "소속 이름") 
	private String compNm;	
	
	@Schema(description = "부서 SEQ") 
	private long deptSeq;
	
	@Schema(description = "부서 이름") 
	private String deptNm;	
	
	@Schema(description = "직급(직책)") 
	private String posiNm;	
	
	@Schema(description = "사용자 구분 코드") 
	private String userGbCd;
	
	@Schema(description = "사용자 구분")
	private String userGb;
	
	@Schema(description = "사용자 업무 코드") 
	private String busiRollCd;
	
	@Schema(description = "사용자 업무")
	private String busiRoll;
	
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
	
	@Schema(description = "검증 여부") 
	private String verifyYn;
	
	@Schema(description = "비밀번호 초기화 여부") 
	private String pwdResetYn;
	
	@Schema(description = "로그인 실패 횟수") 
	private int loginErrCnt;
	
	@Schema(description = "마지막 로그인 IP") 
	private String lastLoginIp;
	
	@Schema(description = "마지막 로그인 시간") 
	private String lastLoginDt;
	
	@Schema(description = "프로필 파일 정보")
	private UploadFileInfo profileFile;
	
	@Schema(description = "작성자 여부") 
	private String ownerYn;
	
	@Schema(description = "사용 여부") 
	private String useYn;
	
	@Schema(description = "삭제 여부") 
	private String delYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;

	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String uptDt;
	
	@Schema(description = "수정자") 
	private String uptNm;	
}
