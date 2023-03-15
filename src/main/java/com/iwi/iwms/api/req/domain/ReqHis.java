package com.iwi.iwms.api.req.domain;

import javax.validation.constraints.NotNull;

import com.iwi.iwms.api.login.domain.LoginUserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqHis {

	@Schema(hidden = true, description = "요청사항 SEQ")
	private long reqSeq;
	
	@NotNull(message = "요청사항 상태 코드는 필수 입력 사항입니다")
	@Schema(description = "요청사항 상태 코드: [01:요청, 02:합의, 03:협의요청, 04:반려, 05:취소]", allowableValues = {"01", "02", "03", "04", "05"}) 
	private String reqStatCd;
	
	@Schema(description = "요청사항 상태 코멘트") 
	private String reqStatCmt;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	public ReqHis of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
