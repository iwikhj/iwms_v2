package com.iwi.iwms.api.req.domain;

import java.util.List;

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
public class ReqDtlInfo {
	
	@Schema(description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(description = "요청사항 번호") 
	private String reqNo;
	
	@Schema(description = "요청사항 상세 SEQ") 
	private long reqDtlSeq;
	
	@Schema(description = "요청사항 상세 번호") 
	private String reqDtlNo;
	
	@Schema(description = "사이트 이름")
	private String siteNm;
	
	@Schema(description = "요청사항 제목") 
	private String reqTitle;
	
	@Schema(description = "요청사항 내용") 
	private String reqContent;
	
	@Schema(description = "요청일")
	private String reqYmd;
	
	@Schema(description = "완료 요청일")
	private String reqEndYmd;
	
	@Schema(description = "요청사항 타입 코드") 
	private String reqTypeCd;
	
	@Schema(description = "요청사항 타입") 
	private String reqType;
	
	@Schema(description = "요청사항 구분 코드") 
	private String reqGbCd;
	
	@Schema(description = "요청사항 구분") 
	private String reqGb;
	
	@Schema(description = "요청사항 이력 SEQ")
	private long reqHisSeq;
	
	@Schema(description = "요청사항 상태 코드") 
	private String reqStatCd;

	@Schema(description = "요청사항 상태") 
	private String reqStat;
	
	@Schema(description = "담당자") 
	private String reqDtlUser;
	
	@Schema(description = "담당자 업무 구분 코드") 
	private String busiRollCd;
	
	@Schema(description = "담당자 업무") 
	private String busiRoll;
	
	@Schema(description = "요청사항 상세 이력 SEQ")
	private long reqDtlHisSeq;
	
	@Schema(description = "요청사항 상세 상태 코드") 
	private String reqDtlStatCd;

	@Schema(description = "요청사항 상세 상태") 
	private String reqDtlStat;

	@Schema(description = "처리 내역 목록") 
	private List<ReqDtlHisInfo> histories;
	
	@Schema(description = "코멘트 목록") 
	private List<ReqDtlCmtInfo> comments;
	
	@Schema(description = "사용 여부")
	private String useYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
}
