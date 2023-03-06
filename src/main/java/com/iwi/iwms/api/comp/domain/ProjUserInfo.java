package com.iwi.iwms.api.comp.domain;

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
public class ProjUserInfo {

	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "소속 SEQ")
	private long compSeq;
	
	@Schema(description = "소속 이름")
	private String compNm;
	
	@Schema(description = "사용자 SEQ")
	private long userSeq;
	
	@Schema(description = "사용자 이름")
	private String userNm;
	
	@Schema(description = "사용자 업무 코드") 
	private String busiRollCd;
	
	@Schema(description = "사용자 업무") 
	private String busiRoll;
	
	@Schema(description = "직급") 
	private String positionNm;
	
	@Schema(description = "프로젝트 담당자 SEQ") 
	private Long projUserSeq;
	
	@Schema(description = "대표자 여부") 
	private String repUserYn;
}