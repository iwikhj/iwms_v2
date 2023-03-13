package com.iwi.iwms.api.auth.domain;

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
public class AuthInfo {
	
	
	@Schema(description = "권한 SEQ")
	private long authSeq;

	@Schema(description = "권한 코드")
	private String authCd;
	
	@Schema(description = "권한 이름")
	private String authNm;
	
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
