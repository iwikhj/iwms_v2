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
public class Req {

	@Schema(hidden = true, description = "요청 SEQ")
	private long reqSeq;
	
	@NotNull
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public Req of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
