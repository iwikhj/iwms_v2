package com.iwi.iwms.config.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReissueResponse {

	@Schema(description = "액세스 토큰")
	@JsonProperty("accessToken")
    private String access_token;

	@Schema(description = "만료 시간(초)")
	@JsonProperty("expiresIn")
	private long expires_in;
}
