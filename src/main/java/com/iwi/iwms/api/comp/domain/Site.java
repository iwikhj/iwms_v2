package com.iwi.iwms.api.comp.domain;

import javax.validation.constraints.NotNull;

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
public class Site {

	@Schema(hidden = true, description = "사이트 SEQ")
	private long siteSeq;
	
	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(description = "소속 SEQ")
	private long compSeq;
	
	@NotNull(message = "프로젝트는 필수 입력 사항입니다")
	@Schema( description = "프로젝트 SEQ")
	private long projSeq;
	
	@NotNull(message = "사이트 이름은 필수 입력 사항입니다")
	@Schema(description = "사이트 이름")
	private String siteNm;
	
	@Schema(description = "사이트 축약 명칭")
	private String siteSwNm;
	
	@NotNull
	@Schema(description = "사이트 구분 코드: [01: 프론트, 02: 관리자, 03: 기타]", allowableValues = {"01", "02", "03"}) 
	private String siteGbCd;
	
	@NotNull
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public Site of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
