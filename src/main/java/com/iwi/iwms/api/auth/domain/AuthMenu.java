package com.iwi.iwms.api.auth.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthMenu {
	
	@Schema(hidden = true, description = "권한 메뉴 SEQ")
	private long authMenuSeq;
	
	@Schema(description = "읽기 권한", allowableValues = {"Y", "N"}) 
	private String readYn;
	
	@Schema(description = "쓰기 권한", allowableValues = {"Y", "N"}) 
	private String writeYn;
	
	@Schema(description = "실행 권한", allowableValues = {"Y", "N"}) 
	private String execYn;
	
	@Schema(description = "사용 여부", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
}
