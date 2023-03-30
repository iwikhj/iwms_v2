package com.iwi.iwms.api.stats.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.stats.domain.ReqRegStatsInfo;
import com.iwi.iwms.api.stats.domain.ReqStatsInfo;

public interface StatsService {
	
	Map<String, Object> listStatsReq(Map<String, Object> map);

	List<ReqRegStatsInfo> listStatsReqRegByMonth(Map<String, Object> map);
}
