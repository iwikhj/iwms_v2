package com.iwi.iwms.api.stats.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.stats.domain.ReqRegStatsInfo;

public interface StatsService {

	List<ReqRegStatsInfo> listStatsReqRegByMonth(Map<String, Object> map);
}
