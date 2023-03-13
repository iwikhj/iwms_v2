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
	
	@Schema(hidden = true, description = "프로젝트 SEQ")
	private long projSeq;
	
	@NotNull(message = "사이트 이름은 필수 입력 사항입니다")
	@Schema(description = "사이트 이름")
	private String siteNm;
	
	@Schema(description = "사이트 축약 명칭")
	private String siteSwNm;
	
	@NotNull
	@Schema(description = "사이트 구분 코드: [HP: 홈페이지 프론트, HA: 홈페이지 관리자, MS: 관리 시스템, ET: 기타 시스템]", allowableValues = {"HP", "HA", "MS", "ET"}) 
	private String siteGbCd;
	
	@NotNull
	@Schema(description = "사용 여부", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long uptSeq;
	
	public Site of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.uptSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
