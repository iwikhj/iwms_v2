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
public class Login {

    @NotNull(message = "사용자 ID는 필수 입력 사항입니다")
	@Schema(description = "사용자 ID") 
    private String username;
    
    @NotNull(message = "비밀번호는 필수 입력 사항입니다")
    @Schema(description = "비밀번호") 
    private String password;
    
    @Schema(hidden = true, description = "사용자 ID 기억(cookie)") 
    private String rememberId;
}
