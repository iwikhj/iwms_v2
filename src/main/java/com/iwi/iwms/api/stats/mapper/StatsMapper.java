package com.iwi.iwms.api.stats.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.stats.domain.ReqRegStatsInfo;
import com.iwi.iwms.api.stats.domain.ReqStatsInfo;

@Mapper
public interface StatsMapper {
	
	List<ReqStatsInfo> listStatsReq(Map<String, Object> map);
	
	List<ReqRegStatsInfo> listStatsReqRegByMonth(Map<String, Object> map);
}
