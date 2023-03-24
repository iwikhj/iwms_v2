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
public class Response<T> {
	
	@Schema(description = "데이터")
    private T data;
	
	@Schema(description = "참조")
	private Map<String, Object> ref;
	
	@Schema(description = "로그인 사용자 정보")
	private LoginUserInfo loginUserInfo;
}
