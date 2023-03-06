package com.iwi.iwms.api.comp.domain;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
public class ProjUserList {

	@NotNull(message = "프로젝트는 필수 입력 사항입니다")
	@Schema(hidden = true, description = "프로젝트 SEQ")
	private long projSeq;
	
	@NotNull(message = "프로젝트 담당자는 필수 입력 사항입니다")
	@Schema(hidden = true, description = "프로젝트 담당자")
	private List<ProjUser> users;
	
	public ProjUserList of(final LoginUserInfo loginUserInfo) {
		if(this.users == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트 담당자는 필수 입력 사항입니다");
		}
		for(int i = 0; i < this.users.size(); i++) {
			ProjUser user = this.users.get(i);
			user.setProjSeq(this.projSeq);
			user.of(loginUserInfo);
		}
		return this;
	}
}
