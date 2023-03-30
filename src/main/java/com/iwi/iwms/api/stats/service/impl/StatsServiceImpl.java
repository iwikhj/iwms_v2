package com.iwi.iwms.api.stats.service.impl;

import java.time.Period;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.stats.domain.ReqRegStatsInfo;
import com.iwi.iwms.api.stats.domain.ReqStatsInfo;
import com.iwi.iwms.api.stats.mapper.StatsMapper;
import com.iwi.iwms.api.stats.service.StatsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

	private final StatsMapper statsMapper;

	@Override
	public Map<String, Object> listStatsReq(Map<String, Object> map) {
		
		List<ReqStatsInfo> listStatsReq = statsMapper.listStatsReq(map);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("01", listStatsReq.stream()
				.filter(v -> Integer.valueOf(v.getStat()) < 10)
				.map(v -> v.getCount())
				.reduce((x, y) -> x + y)
				.orElse(0));
		
		resultMap.put("02", listStatsReq.stream()
				.filter(v -> Integer.valueOf(v.getStat()) == 11)
				.map(v -> v.getCount())
				.reduce((x, y) -> x + y)
				.orElse(0));

		resultMap.put("03", listStatsReq.stream()
				.filter(v -> Integer.valueOf(v.getStat()) == 12 || Integer.valueOf(v.getStat()) == 13)
				.map(v -> v.getCount())
				.reduce((x, y) -> x + y)
				.orElse(0));
		
		resultMap.put("04", listStatsReq.stream()
				.filter(v -> Integer.valueOf(v.getStat()) == 14 )				
				.map(v -> v.getCount())
				.reduce((x, y) -> x + y)
				.orElse(0));	
		
		return resultMap;
	}
	
	@Override
	public List<ReqRegStatsInfo> listStatsReqRegByMonth(Map<String, Object> map) {
		Calendar cal = Calendar.getInstance();
		
		String statsStartYm = (map != null && map.containsKey("statsStartYmd")) ? map.get("statsStartYmd").toString() : cal.get(Calendar.YEAR) + "01";
		String statsEndYm = (map != null && map.containsKey("statsEndYmd")) ? map.get("statsEndYmd").toString() : cal.get(Calendar.YEAR) + "12";
		
		String statsStartYear = statsStartYm.substring(0, 4);
		String statsStartMonth = statsStartYm.substring(4, 6);
		String statsStartDay = "01";
		
		String statsEndYear = statsEndYm.substring(0, 4);
		String statsEndMonth = statsEndYm.substring(4, 6);
		
		cal.set(Integer.parseInt(statsEndYear), Integer.parseInt(statsEndMonth) - 1, 1);
		String statsEndDay = Integer.toString(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		Period diff = Period.between(
				java.time.LocalDate.of(Integer.valueOf(statsStartYear), Integer.valueOf(statsStartMonth), Integer.valueOf(statsStartDay)), 
				java.time.LocalDate.of(Integer.valueOf(statsEndYear), Integer.valueOf(statsEndMonth), Integer.valueOf(statsEndDay)));

		if(diff.getYears() > 0) {
			throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "통계 범위는 최대 1년입니다.");
		}
		if(diff.getMonths() == 0) {
			throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "통계 시작월 또는 종료월이 유효하지 않습니다.");
		}
		
		map.put("statsStartYmd", statsStartYear + statsStartMonth + statsStartDay);
		map.put("statsEndYmd", statsEndYear + statsEndMonth + statsEndDay);		
		
		return statsMapper.listStatsReqRegByMonth(map);
	}

}
