package com.iwi.iwms.api.common.errors;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

	@Schema(description = "http 상태 코드")
    private int status;
	
	@Schema(description = "요청 메소드")
    private String method;
	
	@Schema(description = "요청 경로")
    private String path;
	
	@Schema(description = "에러 메세지")
    private String message;
	
	public String toJson() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Builder
	private ErrorResponse(HttpServletRequest request, int status, String message) {
		this.status = status;
		this.method = request.getMethod();
		this.path = request.getRequestURI();
		this.message = message;
	}
}

