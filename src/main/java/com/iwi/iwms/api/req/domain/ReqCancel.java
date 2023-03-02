package com.iwi.iwms.api.req.domain;

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
public class ReqCancel {

	@Schema(hidden = true, description = "유지보수 SEQ")
	private long reqSeq;
	
	@Schema(hidden = true, description = "삭제 여부", allowableValues = {"Y", "N"}) 
	private String delYn;
	
	@Schema(description = "취소 사유") 
	private String reasonTxt;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public ReqCancel of(final LoginUserInfo loginUserInfo) {
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
