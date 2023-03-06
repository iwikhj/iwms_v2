package com.iwi.iwms.api.req.domain;

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
public class ReqAgree {

	@Schema(hidden = true, description = "요청사항 SEQ")
	private long reqSeq;
	
	@NotNull
	@Schema(description = "합의 여부", defaultValue = "N", allowableValues = {"Y", "N"}) 
	private String agreeYn;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public ReqAgree of(final LoginUserInfo loginUserInfo) {
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
