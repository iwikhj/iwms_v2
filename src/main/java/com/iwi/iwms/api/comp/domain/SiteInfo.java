package com.iwi.iwms.api.comp.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SiteInfo {

	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "사이트 SEQ")
	private long siteSeq;
	
	@Schema(description = "프로젝트 SEQ")
	private long projSeq;
	
	@Schema(description = "사이트 이름")
	private String siteNm;
	
	@Schema(description = "사이트 축약 명칭")
	private String siteSwNm;
	
	@Schema(description = "사이트 구분 코드") 
	private String siteGbCd;
	
	@Schema(description = "사이트 구분") 
	private String siteGb;
	
	@Schema(description = "사용 여부")
	private String useYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String uptDt;
	
	@Schema(description = "수정자") 
	private String uptNm;
}
