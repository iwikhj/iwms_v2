package com.iwi.iwms.api.common.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

	@Schema(description = "http 상태 코드")
    private int status;
	
	@Schema(description = "요청 메소드")
    private String method;
	
	@Schema(description = "요청 경로")
    private String path;
	
	@Schema(description = "데이터")
    private T data;
	
	@Builder
	private ApiResponse(HttpServletRequest request, T data) {
		this.status = HttpServletResponse.SC_OK;
		this.method = request.getMethod();
		this.path = request.getRequestURI();
		this.data = data;
	}
}
