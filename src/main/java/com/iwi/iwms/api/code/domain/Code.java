package com.iwi.iwms.api.code.domain;

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
public class Code {

	@Schema(hidden = true, description = "코드 SEQ")
	private long codeSeq;
	
	@NotNull(message = "코드는 필수 입력 사항입니다.")
	@Schema(description = "코드")
	private String codeCd;
	
	@Schema(description = "코드 이름")
	private String codeNm;
	
	@Schema(description = "상위 코드")
	private String upCodeCd;
	
	@Schema(description = "사용 여부", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public Code of(final LoginUserInfo loginUserInfo) {
		this.loginUserSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
