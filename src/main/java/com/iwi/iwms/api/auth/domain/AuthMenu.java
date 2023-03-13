package com.iwi.iwms.api.auth.domain;

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
public class AuthMenu {
	
	@Schema(hidden = true, description = "권한 메뉴 SEQ")
	private long authMenuSeq;
	
	@Schema(description = "읽기 권한", allowableValues = {"Y", "N"}) 
	private String readYn;
	
	@Schema(description = "쓰기 권한", allowableValues = {"Y", "N"}) 
	private String writeYn;
	
	@Schema(description = "실행 권한", allowableValues = {"Y", "N"}) 
	private String execYn;
	
	@Schema(description = "사용 여부", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long uptSeq;
	
	public AuthMenu of(final LoginUserInfo loginUserInfo) {
		this.uptSeq = loginUserInfo.getUserSeq();
		return this;
	}
	
}
