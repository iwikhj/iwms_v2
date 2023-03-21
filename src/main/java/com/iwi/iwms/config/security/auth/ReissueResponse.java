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

	@Schema(description = "액세스 토큰", name = "access_token")
    protected String accessToken;

	@Schema(description = "만료 시간(초)", name = "expires_in")
    protected long expiresIn;
	
	@Schema(description = "에러")
    protected String error;

	@Schema(description = "에러 메시지", name = "error_description")
    protected String errorDescription;
}
