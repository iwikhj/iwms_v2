package com.iwi.iwms.api.req.domain;

import java.util.List;

import com.iwi.iwms.api.login.domain.LoginInfo;

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
public class ReqDtl {

	@Schema(hidden = true, description = "요청사항 SEQ")
	private Long reqSeq;
	
	@Schema(hidden = true, description = "요청사항 상세 SEQ")
	private Long reqDtlSeq;

	@Schema(hidden = true, description = "요청사항 상세 상태 코드: [11:접수, 12:처리중, 13:처리완료, 14:검수완료, 19:취소]", allowableValues = {"11", "12", "13", "14", "19"}) 
	private String statCd;
	
	@Schema(description = "담당자 SEQ") 
	private List<Long> reqDtlUserSeqs;
	
	@Schema(hidden = true, description = "담당자 SEQ") 
	private Long reqDtlUserSeq;
	
	@Schema(description = "예정 공수")
	private List<Integer> tgtMms;
	
	@Schema(description = "작업 내용")
	private List<String> tasks;
	
	@Schema(hidden = true, description = "예정 공수")
	private Integer tgtMm;
	
	@Schema(hidden = true, description = "작업 내용")
	private String task;
	
	@Schema(description = "처리 시간: 처리완료일때만 입력")
	private Integer procHh;
	
	@Schema(description = "평가 점수: 검수 완료일때만 입력")
	private Integer reqDtlEvalScore;
	
	@Schema(description = "평가 코멘트: 검수 완료일때만 입력")
	private String reqDtlEvalCmt;
	
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;;
	
	public ReqDtl of(final LoginInfo loginInfo) {
		this.loginUserSeq = loginInfo.getUserSeq();
		return this;
	}
}
