package com.iwi.iwms.api.user.domain;

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
public class Menu {
	
	@Schema(description = "메뉴 SEQ")
	private long menuSeq;

	@Schema(description = "사용자 SEQ")
	private long userSeq;

	
}
