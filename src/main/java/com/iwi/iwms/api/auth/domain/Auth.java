package com.iwi.iwms.api.auth.domain;

import javax.validation.constraints.NotNull;

import com.iwi.iwms.api.login.domain.LoginUserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
	
	@Schema(hidden = true, description = "권한 SEQ")
	private long authSeq;

	@NotNull(message = "권한 코드는 필수 입력 사항입니다")
	@Schema(description = "권한 코드")
	private String authCd;
	
	@NotNull(message = "권한 이름은 필수 입력 사항입니다")
	@Schema(description = "권한 이름")
	private String authNm;
	
	@Schema(description = "사용 여부", allowableValues = {"Y", "N"}) 
	private String useYn;

	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long uptSeq;
	
	public Auth of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.uptSeq = loginUserInfo.getUserSeq();
		return this;
	}
	
}
