package com.iwi.iwms.api.stats.domain;

import java.util.List;
import java.util.Map;

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
public class StatsInfo {
	
	@Schema(description = "업무 현황") 
	private Map<String, Object> progStat;
	
	@Schema(description = "월별 등록 건수") 
	private List<ReqRegStatsInfo> regStat;
	
}
