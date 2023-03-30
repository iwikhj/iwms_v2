package com.iwi.iwms.api.stats.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqStatsInfo {
	
	@Schema(description = "진행 상태")
	private String stat;
	
	@Schema(description = "진행 상태별 요청 건수") 
	private Integer count;	
	
}
