package com.iwi.iwms.api.comp.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeptInfo {
	
	@Schema(description = "소속 SEQ")
	private long compSeq;

	@Schema(description = "소속 이름")
	private String compNm;
	
	@Schema(description = "부서 SEQ")
	private long deptSeq;

	@Schema(description = "부서 이름")
	private String deptNm;
	
	@Schema(description = "부서 설명")
	private String deptDesc;
	
	@Schema(description = "작성자 여부") 
	private String ownerYn;
	
	@Schema(description = "사용 여부")
	private String useYn;
	
}
