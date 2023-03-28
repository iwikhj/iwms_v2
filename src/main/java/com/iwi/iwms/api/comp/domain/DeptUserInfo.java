package com.iwi.iwms.api.comp.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeptUserInfo {
	
	@Schema(description = "사용자 SEQ")
	private long userSeq;
	
	@Schema(description = "사용자 이름") 
	private String userNm;	
	
	@Schema(description = "직급(직책)") 
	private String posiNm;	
	
	@Schema(description = "사용자 업무")
	private String busiRoll;
}
