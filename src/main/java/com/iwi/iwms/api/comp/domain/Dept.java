package com.iwi.iwms.api.comp.domain;

import javax.validation.constraints.NotNull;

import com.iwi.iwms.api.login.domain.LoginInfo;

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
public class Dept {

	@Schema(hidden = true, description = "부서 SEQ")
	private long deptSeq;
	
	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(hidden = true, description = "소속 SEQ") 
	private long compSeq;
	
	@NotNull(message = "부서 이름은 필수 입력 사항입니다")
	@Schema(description = "부서 이름")
	private String deptNm;
	
	@Schema(description = "부서 설명") 
	private String deptDesc;
	
	@Schema(description = "상위 부서 SEQ") 
	private Long upDeptSeq;
	
	@Schema(description = "부서 정렬 순서") 
	private int deptOrderNo;
	
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;

	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public Dept of(final LoginInfo loginInfo) {
		this.loginUserSeq = loginInfo.getUserSeq();
		return this;
	}
}
