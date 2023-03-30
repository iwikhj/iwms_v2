package com.iwi.iwms.api.stats.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.stats.domain.ReqRegStatsInfo;

@Mapper
public interface StatsMapper {
	
	List<ReqRegStatsInfo> listStatsReqRegByMonth(Map<String, Object> map);
}
