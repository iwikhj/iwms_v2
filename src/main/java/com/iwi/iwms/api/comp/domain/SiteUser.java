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
public class SiteUser {

	@Schema(hidden = true, description = "사이트별 사용자 SEQ")
	private long siteUserSeq;
	
	@NotNull(message = "사용자는 필수 입력 사항입니다")
	@Schema(hidden = true, description = "소속 SEQ")
	private long userSeq;
	
	@NotNull(message = "프로젝트는 필수 입력 사항입니다")
	@Schema(hidden = true, description = "프로젝트 SEQ")
	private long projSeq;
	
	@NotNull
	@Schema(description = "여부", allowableValues = {"Y", "N"}) 
	private String repUserYn;
	
	@NotNull
	@Schema(description = "업무 롤 코드: [PL: 기획, DS: 디자인, UI: 퍼블리싱, DV: 개발, ET: 기타]", allowableValues = {"PL", "DS", "UI", "DV", "ET"}) 
	private String busiRollCd; 
	
	@NotNull
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	public SiteUser of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
