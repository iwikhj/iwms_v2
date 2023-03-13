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
public class ReqStat {

	@Schema(hidden = true, description = "요청사항 SEQ")
	private long reqSeq;
	
	@NotNull
	@Schema(description = "요청상태 코드: [Y: 합의, N: 협의요청, R: 반려, C: 취소]", allowableValues = {"Y", "N", "R", "C"}) 
	private String reqStatCd;
	
	@Schema(description = "사유") 
	private String reasonTxt;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public ReqStat of(final LoginUserInfo loginUserInfo) {
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
