package com.iwi.iwms.api.req.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.iwi.iwms.api.login.domain.LoginUserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Req {

	@Schema(hidden = true, description = "요청 SEQ")
	private long reqSeq;
	
	@Schema(hidden = true, description = "요청 번호: R{YY}-{000001}") 
	private String reqNo;

	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(hidden = true, description = "소속 SEQ")
	private long compSeq;
	
	@NotNull(message = "프로젝트는 필수 입력 사항입니다")
	@Schema(hidden = true, description = "프로젝트 SEQ")
	private long projSeq;	
	
	@NotNull(message = "사이트는 필수 입력 사항입니다")
	@Schema(hidden = true, description = "사이트 SEQ")
	private long siteSeq;
	
	@NotNull(message = "요청일은 필수 입력 사항입니다")
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "프로젝트 시작일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "요청일: YYYYMMDD")
	private String reqYmd;
	
	@NotNull
	@Schema(description = "요청사항 타입 코드: [00: 일반, 99: 긴급]", allowableValues = {"00", "99"}) 
	private String reqTypeCd;
	
	@NotNull
	@Schema(description = "요청사항 구분 코드: [00: 오류, 01: 개선, 02: 추가, 99: 기타]", allowableValues = {"00", "01", "02", "99"}) 
	private String reqGbCd;

    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "프로젝트 시작일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "요청사항 완료일: YYYYMMDD")
	private String reqEndYmd;
	
	@Schema(description = "요청사항 내용") 
	private String reqContentsTxt;
	
	@NotNull
	@Schema(description = "합의 여부", defaultValue = "N", allowableValues = {"Y", "N"}) 
	private String agreeYn;
	
	@Schema(hidden = true, description = "합의 일시") 
	private String agreeDt;
	
	@Schema(hidden = true, description = "합의자 SEQ") 
	private long agreeUserSeq;
	
	@Schema(hidden = true, description = "삭제(취소) 여부", defaultValue = "N", allowableValues = {"Y", "N"}) 
	private String delYn;
	
	@Schema(hidden = true, description = "삭제(취소) 사유") 
	private String reasonTxt;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public Req of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
