package com.iwi.iwms.api.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserPwd {
	
	@Schema(description = "사용자 SEQ")
	private Long userSeq;
	
	//@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "비밀번호는 영문 숫자 특수문자를 모두 포함하여 공백없이 8 20자로 입력해주세요")
	//@Schema(description = "비밀번호: 8~20자의 숫자, 영문자, 특수문자를 포함") 
	@Schema(description = "비밀번호")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String userPwd;
	
	@Schema(hidden = true, description = "비밀번호 초기화 여부", allowableValues = {"Y", "N"}) 
	private String pwdResetYn;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public UserPwd of(final LoginUserInfo loginUserInfo) {
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
