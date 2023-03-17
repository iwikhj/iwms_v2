package com.iwi.iwms.api.comp.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class Comp {
	
	@Schema(hidden = true, description = "소속 SEQ")
	private long compSeq;
	
	@NotNull(message = "소속 이름은 필수 입력 사항입니다")
	@Schema(description = "소속 이름")
	private String compNm;
	
	@NotNull
	@Schema(description = "소속 구분 코드: [01: 자사, 02: 고객사, 03: 협력사]", allowableValues = {"01", "02", "03"}) 
	private String compGbCd;
	
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호가 아닙니다")
	@Schema(description = "소속 전화번호")
	private String compTel;
	
	@Schema(description = "사용 여부", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public Comp of(final LoginUserInfo loginUserInfo) {
		this.loginUserSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
