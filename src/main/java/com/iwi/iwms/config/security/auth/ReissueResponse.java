package com.iwi.iwms.config.security.auth;

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
    protected String access_token;

	@Schema(description = "만료 시간(초)")
    protected long expires_in;
	
	@Schema(description = "에러")
    protected String error;

	@Schema(description = "에러 메시지")
    protected String errorDescription;
}
