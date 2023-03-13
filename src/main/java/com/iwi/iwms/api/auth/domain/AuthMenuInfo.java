package com.iwi.iwms.api.auth.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class AuthMenuInfo {
	
	@Schema(description = "권한 메뉴 SEQ")
	private long authMenuSeq;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Schema(description = "권한 SEQ")
	private long authSeq;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Schema(description = "메뉴 SEQ")
	private long menuSeq;
	
	@Schema(description = "메뉴 이름")
	private String menuNm;
	
	@Schema(description = "메뉴 설명")
	private String menuDesc;

	@Schema(description = "화면 URL")
	private String pageUrl;
	
	@Schema(description = "화면 이동")
	private String pageTarget;
	
	@Schema(description = "읽기 권한")
	private String readYn;
	
	@Schema(description = "쓰기 권한")
	private String writeYn;
	
	@Schema(description = "실행 권한")
	private String execYn;
	
	@Schema(description = "하위 메뉴") 
	private List<AuthMenuInfo> subMenus;
	
	@Schema(description = "사용 여부") 
	private String useYn;
}
