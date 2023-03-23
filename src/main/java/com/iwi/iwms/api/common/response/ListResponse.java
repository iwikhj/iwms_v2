package com.iwi.iwms.api.common.response;

import java.util.Map;

import com.iwi.iwms.api.login.domain.LoginUserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ListResponse<T> {
	
	@Schema(description = "데이터")
    private T data;
	
	@Schema(description = "검색 조건")
	private Map<String, Object> query;
	
	@Schema(description = "로그인 사용자 정보")
	private LoginUserInfo loginUserInfo;
	
	@Builder
	private ListResponse(Map<String, Object> query, LoginUserInfo loginUserInfo, T data) {
		this.data = data;
		if(query != null && query.containsKey("loginUserSeq")) {
			query.remove("loginUserSeq");
		}
		this.query = query;
		this.loginUserInfo = loginUserInfo;
	}
}
