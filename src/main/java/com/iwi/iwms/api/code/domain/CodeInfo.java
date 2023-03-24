package com.iwi.iwms.api.code.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CodeInfo {

	@Schema(description = "코드")
	private String codeCd;
	
	@Schema(description = "코드 이름")
	private String codeNm;
	
}
