package com.iwi.iwms.api.req.domain;

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
public class ReqInfo {

	@Schema(description = "요청 SEQ")
	private long reqSeq;
	
	@Schema(description = "요청 번호: R{YY}-{000001}") 
	private String reqNo;

	@Schema(description = "소속 SEQ")
	private long compSeq;
	
	@Schema(description = "소속 이름")
	private String compNm;
	
	@Schema(description = "프로젝트 SEQ")
	private long projSeq;	
	
	@Schema(description = "프로젝트 이름")
	private String projNm;
	
	@Schema(description = "사이트 SEQ")
	private long siteSeq;
	
	@Schema(description = "사이트 이름")
	private String siteNm;
	
	@Schema(description = "요청일: YYYYMMDD")
	private String reqYmd;
	
	@Schema(description = "요청사항 타입 코드: [00: 일반, 99: 긴급]") 
	private String reqTypeCd;
	
	@Schema(description = "요청사항 타입") 
	private String reqType;
	
	@Schema(description = "요청사항 구분 코드: [00: 오류, 01: 개선, 02: 추가, 99: 기타]") 
	private String reqGbCd;
	
	@Schema(description = "요청사항 구분") 
	private String reqGb;

	@Schema(description = "요청사항 완료일: YYYYMMDD")
	private String reqEndYmd;
	
	@Schema(description = "요청사항 내용") 
	private String reqContentsTxt;
	
	@Schema(description = "합의 여부") 
	private String agreeYn;
	
	@Schema(description = "합의 일시") 
	private String agreeDt;
	
	@Schema(description = "합의자 SEQ") 
	private long agreeUserSeq;
	
	@Schema(description = "합의자") 
	private String agreeUserNm;
	
	@Schema(description = "삭제(취소) 여부") 
	private String delYn;
	
	@Schema(description = "삭제(취소) 사유") 
	private String reasonTxt;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String updtDt;
	
	@Schema(description = "수정자") 
	private String updtNm;
}
