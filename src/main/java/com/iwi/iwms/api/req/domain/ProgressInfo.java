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
public class ProgressInfo {
	
	@Schema(description = "요청사항 SEQ") 
	private Long reqSeq;
	
	@Schema(description = "요청사항 상세 SEQ") 
	private Long reqDtlSeq;
	
	@Schema(description = "진행")
	private String progress;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;

}
