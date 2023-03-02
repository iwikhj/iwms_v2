package com.iwi.iwms.api.req.domain;

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
public class ReqDtl {

	@Schema(hidden = true, description = "요구사항 SEQ")
	private Long reqDtlSeq;
	
	@Schema(hidden = true, description = "요구사항 번호") 
	private String reqDtlNo;

	@Schema(hidden = true, description = "유지보수 SEQ")
	private long reqSeq;
	
	@Schema(hidden = true, description = "유지보수 번호") 
	private String reqNo;
	
	@Schema(description = "요구사항 제목") 
	private String reqDtlNm;
	
	@Schema(description = "요구사항 내용") 
	private String reqDtlContentTxt;
	
	@Schema(hidden = true, description = "요구사항 사이트 업무 구분 코드") 
	private String dtlSiteGbCd;
	
	@Schema(hidden = true, description = "유지보수 구분 코드: [00: 오류, 01: 개선, 02: 추가, 99: 기타]", allowableValues = {"00", "01", "02", "99"}) 
	private String reqGbCd;
	
	@Schema(hidden = true, description = "사이트 구분 문자열") 
	private String siteGbCdStr;
	
	@Schema(hidden = true, description = "요구사항 상태 코드: [00: 접수, 01: 처리중, 02: 처리완료, 03: 검수완료, 04: 협의요청, 05: 취소]", allowableValues = {"00", "01", "02", "03", "04", "05"}) 
	private String reqDtlStatCd;
	
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "완료 예정일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "완료 예정일: YYYYMMDD")
	private String tgtYmd;
	
	@Schema(description = "완료 예정 공수")
	private Integer tgtMm;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public ReqDtl of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
