package com.iwi.iwms.api.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserProjInfo {

	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "소속 SEQ")
	private long compSeq;
	
	@Schema(description = "프로젝트 SEQ")
	private long projSeq;
	
	@Schema(description = "프로젝트 이름")
	private String projNm;
	
	@Schema(description = "프로젝트 축약 명칭") 
	private String projSwNm;
	
	@Schema(description = "프로젝트 시작일") 
	private String projStdYmd;
	
	@Schema(description = "프로젝트 종료일")
	private String projEndYmd;
}
