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
public class His {

	@Schema(hidden = true, description = "요청사항 SEQ")
	private Long reqSeq;
	
	@Schema(hidden = true, description = "요청사항 상세 SEQ")
	private Long reqDtlSeq;
	
	@Schema(hidden = true, description = "이력 SEQ")
	private Long hisSeq;
	
	@Schema(hidden = true, description = "상태 코드: [01:요청, 02:합의, 03:협의요청(작업수정), 04:협의요청(요청추가), 05:반려, 09:요청취소, 11:접수, 12:처리중, 13:처리완료, 14:검수완료, 19:담당자취소]", allowableValues = {"01", "02", "03", "04", "05", "09", "11", "12", "13", "14", "19"}) 
	private String statCd;
	
	@Schema(description = "요청사항 상태 코멘트") 
	private String statCmt;
	
	@Schema(description = "협의요청 구분: [1:작업수정, 2:요청추가], 협의요청일때만 입력", allowableValues = {"1", "2"})
	private int negoGb;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public His of(final LoginUserInfo loginUserInfo) {
		this.loginUserSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
