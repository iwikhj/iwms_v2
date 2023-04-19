package com.iwi.iwms.config.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IntrospectResponse {

	@Schema(description = "유효성 여부")
    protected boolean active;

	@Schema(description = "인증 서버 아이디")
    protected String sub;
	
	@Schema(description = "인증 서버 세션 아이디")
	@JsonProperty("sessionState")
    protected String session_state;
    
	@Schema(description = "에러")
    protected String error;

	@Schema(description = "에러 메시지")
    protected String errorDescription;
}
