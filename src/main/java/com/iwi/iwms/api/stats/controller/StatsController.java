package com.iwi.iwms.api.stats.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

	@Operation(summary = "월별 등록 건수", description = "월별 등록 건수")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<ReqRegStatsInfo>>> listStatsReqRegByMonth(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo
			, @RequestParam(value = "statsStartYm", required = false, defaultValue = "202301") String statsStartYm
			, @RequestParam(value = "statsEndYm", required = false, defaultValue = "202312") String statsEndYm) {
		
		String statsStartYear = statsStartYm.substring(0, 4);
		String statsStartMonth = statsStartYm.substring(4, 6);
		String statsStartDay = "01";
		
		String statsEndYear = statsEndYm.substring(0, 4);
		String statsEndMonth = statsEndYm.substring(4, 6);
		Calendar cal = Calendar.getInstance();
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
		
		Map<String, Object> map = new HashMap<>();
		map.put("statsStartYmd", statsStartYear + statsStartMonth + statsStartDay);
		map.put("statsEndYmd", statsEndYear + statsEndMonth + statsEndDay);

		List<ReqRegStatsInfo> listStatsReqRegByMonth = statsService.listStatsReqRegByMonth(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<ReqRegStatsInfo>>builder()
				.data(listStatsReqRegByMonth)
				.query(map)
				.build());
	}
}
