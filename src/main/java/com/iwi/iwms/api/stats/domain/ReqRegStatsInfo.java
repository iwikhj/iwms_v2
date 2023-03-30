package com.iwi.iwms.api.stats.domain;

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
public class ReqRegStatsInfo {
	
	@Schema(description = "연월: YYYY-MM")
	private String date;
	
	@Schema(description = "통계 값") 
	private Integer value;	
	
}
