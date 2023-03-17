package com.iwi.iwms.api.comp.domain;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.iwi.iwms.api.login.domain.LoginUserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjUser {

	@Schema(hidden = true, description = "프로젝트 SEQ")
	private long projSeq;
	
	@NotNull(message = "담당자 구분은 필수 입력 사항입니다")
	@Schema(description = "담당자 구분: [1:수행사, 2:고객사]", allowableValues = {"1", "2"})
	private int projUserGb;
	
	@Schema(description = "배정된 담당자 SEQ")
	private List<Long> usersSeq;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public ProjUser of(final LoginUserInfo loginUserInfo) {
		this.loginUserSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
