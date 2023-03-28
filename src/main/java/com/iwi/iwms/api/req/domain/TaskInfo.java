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
public class TaskInfo {
	
	@Schema(description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(description = "작업 SEQ")
	private long reqDtlSeq;
	
	@Schema(description = "작업 구분 코드")
	private String busiRollCd;
	
	@Schema(description = "작업 구분") 
	private String busiRoll;	
	
	@Schema(description = "담당자 SEQ") 
	private long reqDtlUserSeq;
	
	@Schema(description = "담당자")
	private String reqDtlUser;

}
