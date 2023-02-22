package com.iwi.iwms.api.comp.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class Proj {

	@Schema(hidden = true, description = "프로젝트 SEQ")
	private long projSeq;
	
	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(hidden = true, description = "소속 SEQ")
	private long compSeq;
	
	@NotNull(message = "프로젝트 이름은 필수 입력 사항입니다")
	@Schema(description = "프로젝트 이름")
	private String projNm;
	
	@Schema(description = "프로젝트 축약 명칭")
	private String projSwNm;
	
	@NotNull(message = "프로젝트 시작일은 필수 입력 사항입니다")
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "프로젝트 시작일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "프로젝트 시작일: YYYYMMDD")
	private String projStdYmd;
	
	@NotNull(message = "프로젝트 종료일은 필수 입력 사항입니다")
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "프로젝트 종료일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "프로젝트 종료일: YYYYMMDD")
	private String projEndYmd;
	
	@NotNull
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public Proj of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
