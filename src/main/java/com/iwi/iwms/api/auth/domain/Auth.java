package com.iwi.iwms.api.auth.domain;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.iwi.iwms.api.login.domain.LoginInfo;

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
	
	@NotNull(message = "권한 이름은 필수 입력 사항입니다")
	@Schema(description = "권한 이름")
	private String authNm;
	
	@Schema(description = "권한 메뉴 SEQ")
	private List<Long> authMenuSeq;
	
	@Schema(description = "메뉴 읽기 권한") 
	private List<String> menuReadYn;
	
	@Schema(description = "메뉴 쓰기 권한") 
	private List<String> menuWriteYn;
	
	@Schema(description = "메뉴 실행 권한") 
	private List<String> menuExecYn;
	
	@Schema(description = "메뉴 사용 여부") 
	private List<String> menuUseYn;
	
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;

	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public Auth of(final LoginInfo loginInfo) {
		this.loginUserSeq = loginInfo.getUserSeq();
		return this;
	}
	
}
