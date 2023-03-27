package com.iwi.iwms.api.req.domain;

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
public class His {

	@Schema(hidden = true, description = "요청사항 SEQ")
	private Long reqSeq;
	
	@Schema(hidden = true, description = "요청사항 상세 SEQ")
	private Long reqDtlSeq;
	
	@Schema(hidden = true, description = "이력 SEQ")
	private Long hisSeq;
	
	@Schema(hidden = true, description = "상태 코드: [01:요청, 02:합의, 03:협의요청, 04:반려, 05:취소, 11:접수, 12:처리중, 13:처리완료, 14:검수완료, 15:담당자취소]", allowableValues = {"01", "02", "03", "04", "05", "11", "12", "13", "14", "15"}) 
	private String statCd;
	
	@Schema(description = "요청사항 상태 코멘트") 
	private String statCmt;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public His of(final LoginUserInfo loginUserInfo) {
		this.loginUserSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
