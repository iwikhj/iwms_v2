package com.iwi.iwms.api.comp.domain;

import org.springframework.util.StringUtils;

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
public class ProjUser {

	@Schema(hidden = true, description = "프로젝트 SEQ")
	private long projSeq;
	
	@Schema(hidden = true, description = "사용자 SEQ")
	private long userSeq;
	
	@Schema(hidden = true, description = "대표자 여부", defaultValue = "N", allowableValues = {"Y", "N"}) 
	private String repUserYn;
	
	@Schema(hidden = true, description = "사용자 업무 코드: [PN: 기획, DS: 디자인, UI: 퍼블리싱, DV: 개발, DB: DB, ET: 기타]", allowableValues = {"PN", "DS", "UI", "DV", "DB", "ET"}) 
	private String busiRollCd;
	
	@Schema(hidden = true, description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long uptSeq;
	
	public ProjUser of(final LoginUserInfo loginUserInfo) {
		this.repUserYn = StringUtils.hasText(this.repUserYn) ? this.repUserYn : "N";
		this.useYn = StringUtils.hasText(this.useYn) ? this.useYn : "Y";
		this.regSeq = loginUserInfo.getUserSeq();
		this.uptSeq = loginUserInfo.getUserSeq();
		return this;
	}
}
