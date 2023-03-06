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
public class ReqDtlUserInfo {

	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "요청사항 상세 담당자 SEQ")
	private Long reqDtlUserSeq;
	
	@Schema(description = "요청사항 상세 담당자 코멘트 SEQ")
	private Long reqDtlUserCmtSeq;
	
	@Schema(description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(description = "요청사항 번호") 
	private String reqNo;
	
	@Schema(description = "요청사항 상세 SEQ")
	private long reqDtlSeq;
	
	@Schema(description = "요청사항 상세 번호") 
	private String reqDtlNo;
	
	@Schema(description = "요청사항 상세 담당자 SEQ") 
	private long userSeq;
	
	@Schema(description = "요청사항 상세 담당자") 
	private String userNm;

	@Schema(description = "요청사항 상세 담당자 업무 코드") 
	private String busiRollCd;
	
	@Schema(description = "요청사항 상세 담당자 업무") 
	private String busiRoll;
	
	@Schema(description = "처리 예정일")
	private String procYmd;
	
	@Schema(description = "처리 예정 시간")
	private Integer procHh;
	
	@Schema(description = "요청사항 상세 시작 예정일")
	private String reqDtlStdYmd;
	
	@Schema(description = "요청사항 상세 시작 시간")
	private String reqDtlStdPart;
	
	@Schema(description = "요청사항 상세 종료 예정일")
	private String reqDtlEndYmd;
	
	@Schema(description = "요청사항 상세 종료 시간")
	private String reqDtlEndPart;
	
	@Schema(description = "요청사항 상세 담당자 코멘트")
	private String cmt;
	
	@Schema(description = "메인 매니저 담당 여부") 
	private String mngMainYn;
	
	@Schema(description = "요청사항 상세 상태 코드") 
	private String reqDtlStatCd;
	
	@Schema(description = "요청사항 상세 상태") 
	private String reqDtlStat;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
}
