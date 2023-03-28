package com.iwi.iwms.api.common.errors;

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
	
	@Schema(description = "에러 코드")
    private String code;
	
	@Schema(description = "에러 메시지")
    private String message;
	
	public String toJson() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@Builder
	private ErrorResponse(ErrorCode code, String message) {
		this.code = code.getCode();
		this.message = code.getMessage(code, message);
	}
}

