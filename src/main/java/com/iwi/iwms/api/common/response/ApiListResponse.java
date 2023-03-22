package com.iwi.iwms.api.common.response;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiListResponse<T> {

	@Schema(description = "데이터")
    private T data;
	
	@Schema(description = "검색 조건")
	private Map<String, Object> query;
}
