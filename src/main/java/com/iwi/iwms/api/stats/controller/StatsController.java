package com.iwi.iwms.api.stats.controller;

import java.time.Period;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.stats.domain.ReqRegStatsInfo;
import com.iwi.iwms.api.stats.service.StatsService;
import com.iwi.iwms.utils.PredicateMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Statistics", description = "IWMS 유지보수 통계")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.root}/${app.version}/statistics")
public class StatsController {

	private final StatsService statsService;

	@Operation(summary = "유지 보수 현황", description = "유지 보수 현황. 현황 구분: [01:요청, 02:접수, 03:진행중, 04:완료]")
	@GetMapping(value = "/total")
	public ResponseEntity<ApiListResponse<Map<String, Object>>> listStatsReqRegByMonth(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo) {
		
		Map<String, Object> map = PredicateMap.make(request, loginUserInfo);
		Map<String, Object> resultMap = statsService.listStatsReq(map);
						
		return ResponseEntity.ok(ApiListResponse.<Map<String, Object>>builder()
				.data(resultMap)
				.query(map)
				.build());
	}
	
	@Operation(summary = "월별 등록 건수", description = "월별 등록 건수")
	@GetMapping(value = "/monthly")
	public ResponseEntity<ApiListResponse<List<ReqRegStatsInfo>>> listStatsReqRegByMonth(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @RequestParam(value = "statsStartYm", required = false, defaultValue = "202301") String statsStartYm
			, @RequestParam(value = "statsEndYm", required = false, defaultValue = "202312") String statsEndYm) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("statsStartYmd", statsStartYm);
		map.put("statsEndYmd", statsEndYm);

		List<ReqRegStatsInfo> listStatsReqRegByMonth = statsService.listStatsReqRegByMonth(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<ReqRegStatsInfo>>builder()
				.data(listStatsReqRegByMonth)
				.query(map)
				.build());
	}
}
