package com.iwi.iwms.api.stats.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.iwi.iwms.api.stats.domain.ReqRegStatsInfo;
import com.iwi.iwms.api.stats.mapper.StatsMapper;
import com.iwi.iwms.api.stats.service.StatsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

	private final StatsMapper statsMapper;
	
	@Override
	public List<ReqRegStatsInfo> listStatsReqRegByMonth(Map<String, Object> map) {
		return statsMapper.listStatsReqRegByMonth(map);
	}

}
