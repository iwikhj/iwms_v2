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
public class ReqTaskInfo {
	
	@Schema(description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(description = "요청사항 상세 SEQ")
	private long reqDtlSeq;
	
	@Schema(description = "요청사항 상세 번호") 
	private String reqDtlNo;
	
	@Schema(description = "작업 담당자 이름") 
	private String reqDtlUserNm;

	@Schema(description = "시작일자") 
	private String reqDtlStdYmd;
	
	@Schema(description = "종료일자") 
	private String reqDtlEndYmd;
	
	@Schema(description = "요청사항 상세 업무 코드") 
	private String busiRollCd;
	
	@Schema(description = "요청사항 상세 업무") 
	private String busiRoll;
	
	@Schema(description = "요청사항 상세 상태 코드") 
	private String reqDtlStatCd;
	
	@Schema(description = "요청사항 상세 상태") 
	private String reqDtlStat;
	
	@Schema(description = "평가 점수") 
	private String reqDtlEvalScore;
	
	@Schema(description = "평가 내용") 
	private String reqDtlEvalCmt;

	@Schema(description = "사용 여부")
	private String useYn;

}
