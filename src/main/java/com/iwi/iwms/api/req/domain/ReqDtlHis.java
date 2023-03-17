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
public class ReqDtlHis {

	@Schema(hidden = true, description = "요청사항 상세 SEQ")
	private long reqDtlSeq;
	
	@NotNull(message = "요청사항 상세 상태 코드는 필수 입력 사항입니다")
	@Schema(description = "요청사항 상세 상태 코드: [11:접수, 12:처리중, 13:처리완료, 14:검수완료, 15:취소]", allowableValues = {"11", "12", "13", "14", "15"}) 
	private String reqDtlStatCd;
	
	@Schema(description = "요청사항 상세 상태 코멘트") 
	private String reqDtlStatCmt;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public ReqDtlHis of(final LoginUserInfo loginUserInfo) {
		this.loginUserSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
