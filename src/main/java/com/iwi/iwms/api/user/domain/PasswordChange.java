package com.iwi.iwms.api.user.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.BeanUtils;

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
public class PasswordChange {
	
	@Schema(hidden = true, description = "사용자 SEQ")
	private long userSeq;
	
	@NotNull(message = "비밀번호는 필수 입력 사항입니다")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "비밀번호는 영문 숫자 특수문자를 모두 포함하여 공백없이 8 20자로 입력해주세요")
	@Schema(description = "비밀번호: 8~20자의 숫자, 영문자, 특수문자를 포함") 
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String userPwd;
	
	@Schema(hidden = true, description = "비밀번호 초기화 여부", allowableValues = {"Y", "N"}) 
	private String pwdResetYn;
	
	@Schema(hidden = true, description = "IWI 통합 인증 ID")
	private String ssoId;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public PasswordChange ofChange(final LoginUserInfo loginUserInfo) {
		this.userSeq = loginUserInfo.getUserSeq();
		this.pwdResetYn = "N";
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
	
	public PasswordChange ofReset(final LoginUserInfo loginUserInfo) {
		this.pwdResetYn = "Y";
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
	
	public User asUser() {
		User user = new User();
		BeanUtils.copyProperties(this, user);
		return user;
	}
}
