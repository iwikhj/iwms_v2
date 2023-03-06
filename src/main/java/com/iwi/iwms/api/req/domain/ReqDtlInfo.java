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
	
	@Schema(description = "번호")
	private long rowNum;

	@Schema(description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(description = "요청사항 번호") 
	private String reqNo;
	
	@Schema(description = "요청사항 상세 SEQ")
	private Long reqDtlSeq;
	
	@Schema(description = "요청사항 상세 번호") 
	private String reqDtlNo;
	
	@Schema(description = "요청사항 상세 제목") 
	private String reqDtlNm;
	
	@Schema(description = "요청사항 상세 내용") 
	private String reqDtlContentTxt;
	
	@Schema(description = "요청사항 상세 사이트 업무 구분 코드") 
	private String dtlSiteGbCd;
	
	@Schema(description = "요청사항 상세 사이트 업무 구분") 
	private String dtlSiteGb;
	
	@Schema(description = "요청사항 구분 코드") 
	private String reqGbCd;
	
	@Schema(description = "요청사항 구분") 
	private String reqGb;
	
	@Schema(description = "사이트 구분 문자열") 
	private String siteGbCdStr;
	
	@Schema(description = "요청사항 상세 상태 코드") 
	private String reqDtlStatCd;
	
	@Schema(description = "요청사항 상세 상태") 
	private String reqDtlStat;
	
	@Schema(description = "요청사항 상세 상태 코멘트") 
	private String reqDtlStatCmt;
	
	@Schema(description = "요청사항 상세 완료 예정일")
	private String tgtYmd;
	
	@Schema(description = "요청사항 상세 완료 예정 공수") 
	private String tgtMm;
	
	@Schema(description = "요청사항 상세 관리자 확인일")
	private String mngOkYmd;
	
	@Schema(description = "요청사항 상세 확인 관리자 SEQ") 
	private long mngOkSeq;
	
	@Schema(description = "요청사항 상세 고객 확인일")
	private String cusOkYmd;
	
	@Schema(description = "요청사항 상세 확인 고객 SEQ") 
	private long cusOkSeq;
	
	@Schema(description = "요청사항 상세 반송일")
	private String returnYmd;
	
	@Schema(description = "요청사항 상세 반송자 SEQ") 
	private long returnSeq;
	
	@Schema(description = "요청사항 상세 평가 점수") 
	private int reqDtlScore;
	
	@Schema(description = "요청사항 상세 평가 내용") 
	private String reqDtlEvalCotentCmt;
	
	@Schema(description = "요청사항 상세 코멘트") 
	private List<ReqDtlCmtInfo> comments;
	
	@Schema(description = "요청사항 상세 담당자 코멘트") 
	private List<ReqDtlUserInfo> users;
	
	@Schema(description = "삭제 여부")
	private String delYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String updtDt;
	
	@Schema(description = "수정자") 
	private String updtNm;
}
