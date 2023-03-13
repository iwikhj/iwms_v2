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
public class ReqDtlUser {

	@Schema(hidden = true, description = "요청사항 상세 담당자 SEQ")
	private Long reqDtlUserSeq;
	
	@Schema(hidden = true, description = "요청사항 상세 담당자 코멘트 SEQ")
	private Long reqDtlUserCmtSeq;
	
	@Schema(hidden = true, description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(hidden = true, description = "요청사항 상세 SEQ")
	private long reqDtlSeq;
	
	@Schema(hidden = true, description = "요청사항 상세 담당자 SEQ") 
	private long userSeq;

	@Schema(hidden = true, description = "업무 롤 코드: [PN: 기획, DS: 디자인, UI: 퍼블리싱, DV: 개발, DB: DB, ET: 기타]", allowableValues = {"PN", "DS", "UI", "DV", "DB", "ET"}) 
	private String busiRollCd;
	
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "처리 예정일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "처리 예정일: YYYYMMDD")
	private String procYmd;
	
	@Schema(description = "처리 예정 시간")
	private Integer procHh;
	
	@Schema(description = "요청사항 상세 시작 예정일")
	private String reqDtlStdYmd;
	
	@Schema(description = "요청사항 상세 시작 시간", allowableValues = {"A", "P"})
	private String reqDtlStdPart;
	
	@Schema(description = "요청사항 상세 종료 예정일")
	private String reqDtlEndYmd;
	
	@Schema(description = "요청사항 상세 종료 시간", allowableValues = {"A", "P"})
	private String reqDtlEndPart;
	
	@Schema(description = "요청사항 상세 담당자 코멘트")
	private String cmt;
	
	@NotNull
	@Schema(description = "메인 매니저 담당 여부", defaultValue = "N", allowableValues = {"Y", "N"}) 
	private String mngMainYn;
	
	@NotNull
	@Schema(description = "요청사항 상세 상태 코드: [00: 접수, 01: 처리중, 02: 처리완료, 03: 검수완료, 04: 협의요청, 05: 취소]", allowableValues = {"00", "01", "02", "03", "04", "05"}) 
	private String reqDtlStatCd;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	public ReqDtlUser of(final LoginUserInfo loginUserInfo) {
		this.userSeq = loginUserInfo.getUserSeq();
		this.regSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
