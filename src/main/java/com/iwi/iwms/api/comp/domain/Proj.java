package com.iwi.iwms.api.comp.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.iwi.iwms.api.login.domain.LoginInfo;

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
public class Proj {

	@Schema(hidden = true, description = "프로젝트 SEQ")
	private long projSeq;
	
	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(description = "소속 SEQ")
	private long compSeq;
	
	@NotNull(message = "프로젝트 이름은 필수 입력 사항입니다")
	@Schema(description = "프로젝트 이름")
	private String projNm;
	
	@Schema(description = "프로젝트 축약 명칭")
	private String projSwNm;
	
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "프로젝트 시작일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "프로젝트 시작일: YYYYMMDD")
	private String projStdYmd;
	
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "프로젝트 종료일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "프로젝트 종료일: YYYYMMDD")
	private String projEndYmd;
	
	@NotNull
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public Proj of(final LoginInfo loginInfo) {
		this.loginUserSeq = loginInfo.getUserSeq();
		return this;
	}
}
