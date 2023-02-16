package com.iwi.iwms.api.comp.domain;

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
public class Position {

	@Schema(hidden = true, description = "직급 SEQ")
	private long positionSeq;

	@NotNull(message = "직급 이름은 필수 입력 사항입니다")
	@Schema(description = "직급 이름")
	private String positionNm;
	
	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(hidden = true, description = "소속 SEQ") 
	private long compSeq;
	
	@NotNull
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;

	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public Position of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
