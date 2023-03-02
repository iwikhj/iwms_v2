package com.iwi.iwms.api.code.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChildCodeInfo {

	@Schema(description = "코드 SEQ")
	private long codeSeq;
	
	@Schema(description = "코드")
	private String codeCd;
	
	@Schema(description = "코드 이름")
	private String codeNm;
	
	@Schema(description = "코드 구분")
	private String codeGbCd;
	
	@Schema(description = "코드 순서")
	private int codeOrder;
	
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
