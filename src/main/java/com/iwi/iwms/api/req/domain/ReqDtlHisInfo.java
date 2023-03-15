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
public class ReqDtlHisInfo {

	@Schema(description = "요청사항 SEQ") 
	private long reqSeq;
	
	@Schema(description = "요청사항 상세 SEQ") 
	private Long reqDtlSeq;
	
	@Schema(description = "요청사항 상세 상태 코드") 
	private String reqDtlStatCd;
	
	@Schema(description = "요청사항 상세 상태") 
	private String reqDtlStat;
	
	@Schema(description = "요청사항 상태 코멘트") 
	private String reqDtlStatCmt;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
}
