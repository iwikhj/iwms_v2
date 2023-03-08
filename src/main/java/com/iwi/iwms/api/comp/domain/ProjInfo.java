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
public class ProjInfo {

	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "프로젝트 SEQ")
	private long projSeq;
	
	@Schema(description = "소속 SEQ")
	private long compSeq;
	
	@Schema(description = "소속 이름")
	private String compNm;
	
	@Schema(description = "프로젝트 이름")
	private String projNm;
	
	@Schema(description = "프로젝트 축약 명칭")
	private String projSwNm;
	
	@Schema(description = "프로젝트 시작일: YYYYMMDD")
	private String projStdYmd;
	
	@Schema(description = "프로젝트 종료일: YYYYMMDD")
	private String projEndYmd;
	
	@Schema(description = "사용 여부")
	private String useYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String updtDt;
	
	@Schema(description = "수정자") 
	private String updtNm;
}
