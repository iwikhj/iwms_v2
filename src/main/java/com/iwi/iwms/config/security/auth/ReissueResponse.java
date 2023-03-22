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

	@Schema(hidden = true, description = "액세스 토큰")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String access_token;

	@Schema(hidden = true, description = "만료 시간(초)")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private long expires_in;
	
	@Schema(description = "액세스 토큰")
	private String accessToken;
	
	@Schema(description = "만료 시간(초)")
	private long expiresIn;
	
	public ReissueResponse of() {
		this.accessToken = this.access_token;
		this.expiresIn = this.expires_in;
		return this;
	}
}
