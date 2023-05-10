package com.iwi.iwms.api.common.response;

import com.iwi.iwms.api.login.domain.LoginInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponse<T> {
	
	@Schema(description = "데이터")
    private T data;
	
	@Schema(description = "로그인 정보")
	private LoginInfo loginInfo;
}
