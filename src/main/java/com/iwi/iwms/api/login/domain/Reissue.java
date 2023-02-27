package com.iwi.iwms.api.login.domain;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reissue {

    @NotNull(message = "리프레시 토큰은 필수 입력 사항입니다")
	@Schema(description = "리프레시 토큰") 
    private String refreshToken;
    
}
