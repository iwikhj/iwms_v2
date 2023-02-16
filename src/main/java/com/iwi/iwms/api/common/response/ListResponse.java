package com.iwi.iwms.api.common.response;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iwi.iwms.api.login.domain.LoginUserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ListResponse<T> {

	@Schema(description = "http 상태 코드")
    private int status;
	
	@Schema(description = "요청 메소드")
    private String method;
	
	@Schema(description = "요청 경로")
    private String path;
	
	@Schema(description = "데이터")
    private T data;
	
	@Schema(description = "검색 조건")
	private Map<String, Object> query;
	
	@Schema(description = "로그인 사용자 정보")
	private LoginUserInfo loginUserInfo;
	
	@Builder
	private ListResponse(HttpServletRequest request, T data, Map<String, Object> query, LoginUserInfo loginUserInfo) {
		this.status = HttpServletResponse.SC_OK;
		this.method = request.getMethod();
		this.path = request.getRequestURI();
		this.data = data;
		this.query = query;
		this.loginUserInfo = loginUserInfo;
	}
}
