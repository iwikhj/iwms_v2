package com.iwi.iwms.api.code.domain;

import java.util.List;

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

	@Schema(description = "코드 SEQ")
	private long codeSeq;
	
	@Schema(description = "코드")
	private String codeCd;
	
	@Schema(description = "코드 이름")
	private String codeNm;
	
	@Schema(description = "상위 코드")
	private String upCodeCd;
	
	@Schema(description = "작성자 여부") 
	private String ownerYn;	
	
	@Schema(description = "사용 여부") 
	private String useYn;
	
	@Schema(description = "하위 코드 목록") 
	private List<CodeInfo> subCodes;
	
}
