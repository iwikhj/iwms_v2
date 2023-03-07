package com.iwi.iwms.api.user.domain;

import java.util.List;

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
public class MenuInfo {

	@Schema(description = "메뉴 SEQ")
	private long menuSeq;

	@Schema(description = "메뉴 이름")
	private String menuNm;
	
	@Schema(description = "메뉴 순번")
	private int menuOrderNo;
	
	@Schema(description = "메뉴 설명")
	private String menuDesc;
	
	@Schema(description = "페이지 URL")
	private String pageUrl;
	
	@Schema(description = "페이지 Target")
	private String pageTarget;
	
	@Schema(description = "메뉴 권한 여부") 
	private String hasPermission;
	
	@Schema(description = "하위 메뉴") 
	private List<MenuInfo> subMenus;
}
